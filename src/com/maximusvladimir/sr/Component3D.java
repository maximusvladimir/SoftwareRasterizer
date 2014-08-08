package com.maximusvladimir.sr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.maximusvladimir.sr.math.ImageData;

public class Component3D extends Component {
	private static final long serialVersionUID = 664343555845197174L;
	private BufferedImage _buffer;
	private Boolean _isRendering = (Boolean) false;
	private Object _lock = new Object();
	private long _lastFPSSample;
	private int _framesDrawn = 0;
	private int _lastFramesDrawn = 0;
	private float _frameTime = 0;
	private long _realFrameTimeTotal = 0;
	private float _realFrameTime = 0;
	private boolean _showFPS = true;
	private Timer _logicUpdater;
	private RGB _clearColor = RGB.CornflowerBlue;
	private BufferedImage _depth;
	private GL _gl;
	private Runnable _drawLoop = null;
	private BufferedImage _lastGoodImage;
	private ImageData _bufferData;
	private long _lastMove = 0;
	private float _quality;

	public Component3D() {
		this(false, 30, null,1);
	}

	public Component3D(boolean allowClearBuffer, int targettedFPS,
			Runnable drawLoop, float quality) {
		setSize(100, 100);
		_drawLoop = drawLoop;
		_gl = new GL();
		_quality = quality;
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent arg0) {
				recreateBuffer();
			}
		});

		ActionListener updateTimer = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (_isRendering)
					return;
				synchronized (_lock) {
					_lock.notifyAll();
				}
				if (System.currentTimeMillis() - _lastMove > 200)
					repaint();
			}
		};
		_logicUpdater = new Timer((int) (1000.0 / targettedFPS), updateTimer);
		_logicUpdater.start();

		recreateBuffer();

		Thread renderThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					_isRendering = (Boolean) true;
					long start = System.nanoTime();
					render(_buffer.getGraphics());
					//_lastGoodImage = deepCopy(_depth);
					_lastGoodImage = deepCopy(_buffer);
					_realFrameTimeTotal += (System.nanoTime() - start);
					_framesDrawn++;
					_isRendering = (Boolean) false;
					try {
						synchronized (_lock) {
							_lock.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		renderThread.setName("render");
		renderThread.setPriority(Thread.MAX_PRIORITY);
		renderThread.start();

		java.util.Timer t = new java.util.Timer();
		t.schedule(new java.util.TimerTask() {
			public void run() {
				Window wnd = SwingUtilities.getWindowAncestor(Component3D.this);
				if (wnd != null) {
					wnd.addComponentListener(new ComponentAdapter() {
						public void componentMoved(ComponentEvent evt) {
							_lastMove = System.currentTimeMillis();
						}
					});
				}
			}
		}, 1000);
	}
	
	public void setQuality(float q) {
		_quality = q;
		recreateBuffer();
	}
	
	public float getQuality() {
		return _quality;
	}

	public void setDrawLoop(Runnable loop) {
		_drawLoop = loop;
	}

	public Runnable getDrawLoop() {
		return _drawLoop;
	}

	public void setTargettedFPS(int fps) {
		_logicUpdater.setDelay((int) (1000.0 / fps));
	}

	public int getTargettedFPS() {
		return (int) (1000.0 / _logicUpdater.getDelay());
	}

	public void setFPSVisible(boolean v) {
		_showFPS = v;
	}

	public boolean isFPSVisible() {
		return _showFPS;
	}

	public GL getGL() {
		return _gl;
	}

	private void render(Graphics g) {
		if (_drawLoop != null) {
			_drawLoop.run();
		}
		_bufferData.gz.setColor(Color.black);
		_bufferData.gz.fillRect(0, 0, _bufferData.w, _bufferData.h);
		g.setColor(new Color(_clearColor.rgb()));
		g.fillRect(0, 0, _bufferData.w,_bufferData.h);
		_gl.draw(_bufferData);
	}

	public void recreateBuffer() {
		int w = (int)(getWidth() * _quality);
		if (w == 0)
			w++;
		int h = (int)(getHeight() * _quality);
		if (h == 0)
			h++;
		_buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		_bufferData = new ImageData();
		_bufferData.w = _buffer.getWidth();
		_bufferData.h = _buffer.getHeight();
		_bufferData.g = _buffer.getGraphics();
		_bufferData.data = ((DataBufferInt) _buffer.getRaster().getDataBuffer())
				.getData();
		_depth = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		_bufferData.zbuffer = ((DataBufferInt)_depth.getRaster().getDataBuffer()).getData();
		_bufferData.gz = _depth.getGraphics();
	}

	private static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public void paint(Graphics g) {
		if (System.currentTimeMillis() - _lastFPSSample >= 1000) {
			if (_framesDrawn != 0) {
				_frameTime = ((float) (System.currentTimeMillis() - _lastFPSSample))
						/ _framesDrawn;
				_realFrameTime = Math.round(_realFrameTimeTotal / _framesDrawn
						/ 1000000.0f * 10000) / 10000.0f;
			}
			_lastFPSSample = System.currentTimeMillis();
			_lastFramesDrawn = _framesDrawn;
			_framesDrawn = 0;
			_realFrameTimeTotal = 0;
		}

		if (_quality < 1)
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		else if (_quality > 1)
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_lastGoodImage, 0, 0, getWidth(),getHeight(), null);
		if (isFPSVisible()) {
			g.setColor(Color.white);
			g.drawString("FPS:" + _lastFramesDrawn, 0, 10);
			g.drawString("Frame time: " + _frameTime + " ms", 0, 20);
			g.drawString("Real frame time: " + _realFrameTime + " ms", 0, 30);
		}
	}
}
