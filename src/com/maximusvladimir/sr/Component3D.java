package com.maximusvladimir.sr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import com.maximusvladimir.sr.math.ImageData;

public class Component3D extends Component {
	private static final long serialVersionUID = 664343555845197174L;
	private BufferedImage _buffer;
	private Boolean _isRendering = (Boolean) false;
	private Object _lock = new Object();
	private long _currentFrame = 0;
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
	private Runnable _initLoop = null;
	private Runnable _depthBuffer = null;
	private Thread _depthBufferState;
	private Object _dephtBufferStateLock = new Object();
	private BufferedImage _lastGoodImage;
	private ImageData _bufferData;
	private long _lastMove = 0;
	private float _quality;
	private boolean _fullscreen = false;
	private int _fullScreenMemoryStateUndecorated = (byte) -1;
	private int _fullScreenMemoryStateWidth = -1;
	private int _fullScreenMemoryStateHeight = -1;
	private boolean _fullScreenMemoryStateVisible = false;
	private AtomicBoolean _isInRender = new AtomicBoolean(false);
	private AtomicBoolean _isSubjectToCreation = new AtomicBoolean(false);

	public Component3D() {
		this(false, 30, null, 1);
	}

	public Component3D(boolean allowClearBuffer, int targettedFPS,
			Runnable drawLoop, float quality) {
		setSize(100, 100);
		_drawLoop = drawLoop;
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
					while (_isSubjectToCreation.get())
					{
					
					}
					_isRendering = (Boolean) true;
					long start = System.nanoTime();
					_isInRender.set(true);
					render(_buffer.getGraphics());
					synchronized (_dephtBufferStateLock) {
						_dephtBufferStateLock.notify();
					}
					//_lastGoodImage = deepCopy(_depth);
					_lastGoodImage = deepCopy(_buffer);
					_isInRender.set(false);
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
		_gl = new GL(renderThread.getId());
		renderThread.setName("render");
		renderThread.setPriority(Thread.MAX_PRIORITY);
		renderThread.start();
		
		_depthBufferState = new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (getDepthRenderLoop() != null)
						getDepthRenderLoop().run();
					try {
						synchronized (_dephtBufferStateLock) {
							_dephtBufferStateLock.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		_depthBufferState.setName("depthbufferpoll");
		_depthBufferState.start();

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

	public void setFullscreen(boolean fullscreen, int w, int h) {
		setFullscreen(fullscreen,w,h,0);
	}
	
	public void setFullscreen(boolean fullscreen, int w, int h, int screen) {
		Window wnd = SwingUtilities.getWindowAncestor(Component3D.this);
		if (wnd != null) {
			if (!_fullscreen && fullscreen) {
				GraphicsEnvironment ge = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				GraphicsDevice[] devices = ge.getScreenDevices();
				if (devices.length == 0) {
					System.err.println("Unable to find graphics device.");
					return;
				}
				if (screen >= devices.length || screen < 0) {
					System.err.println("Invalid screen.");
					return;
				}
				GraphicsDevice device = ge.getScreenDevices()[screen];
				if (!device.isFullScreenSupported())
					return;
				DisplayMode[] modes = device.getDisplayModes();
				double closest = 100000;
				int index = -1;
				boolean usingClosest = true;
				for (int i = 0; i < modes.length; i++) {
					DisplayMode m = modes[i];
					double dist = Math.sqrt(Math.pow(m.getWidth()-w,2)+Math.pow(m.getHeight()-h, 2));
					if (dist <= 1) {
						index = i;
						usingClosest = false;
						break;
					}
					if (dist < closest) {
						closest = dist;
						index = i;
					}
				}
				if (index == -1) {
					System.err.println("Unable to find a suitable display mode.");
					return;
				}
				w = modes[index].getWidth();
				h = modes[index].getHeight();
				if (usingClosest) {
					System.err.println("Unable to find display mode at provided resolution. Falling back to ("+w+", "+h+").");
				}
				try {
					_fullScreenMemoryStateVisible = wnd.isVisible();
					_fullScreenMemoryStateWidth = wnd.getWidth();
					_fullScreenMemoryStateHeight = wnd.getHeight();
					wnd.setVisible(false);
					wnd.dispose();
					if (wnd instanceof Frame) {
						_fullScreenMemoryStateUndecorated = ((Frame) wnd)
								.isUndecorated() ? 1 : 0;
						((Frame) wnd).setUndecorated(true);
					}
					wnd.setVisible(true);
					device.setFullScreenWindow(wnd);
					device.setDisplayMode(new DisplayMode(w,h, 32, 60));
					_fullscreen = fullscreen;
				} catch (Throwable t) {

				}
			} else if (!fullscreen && _fullscreen) {
				wnd.setVisible(false);
				wnd.dispose();
				if (_fullScreenMemoryStateUndecorated != -1)
					((Frame)wnd).setUndecorated(_fullScreenMemoryStateUndecorated == 1);
				wnd.setVisible(true);
				GraphicsEnvironment ge = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				GraphicsDevice device = ge.getScreenDevices()[0];
				device.setFullScreenWindow(null);
				wnd.setSize(_fullScreenMemoryStateWidth, _fullScreenMemoryStateHeight);
				wnd.setVisible(_fullScreenMemoryStateVisible);
				_fullscreen = false;
			}
		}
	}

	public boolean isFullscreen() {
		return _fullscreen;
	}

	public void setQuality(float q) {
		_quality = q;
		recreateBuffer();
	}

	public float getQuality() {
		return _quality;
	}
	
	public void setInitProc(Runnable proc) {
		_initLoop = proc;
	}
	
	public Runnable getInitProc() {
		return _initLoop;
	}

	public void setDrawLoop(Runnable loop) {
		_drawLoop = loop;
	}

	public Runnable getDrawLoop() {
		return _drawLoop;
	}
	
	public void setDepthRenderLoop(Runnable loop) {
		_depthBuffer = loop;
	}
	
	public Runnable getDepthRenderLoop() {
		return _depthBuffer;
	}
	
	public BufferedImage getDepthBuffer() {
		return _depth;
	}

	public void setTargettedFPS(int fps) {
		_logicUpdater.setDelay((int) (1000.0 / fps));
	}

	public int getTargettedFPS() {
		return (int) (1000.0 / _logicUpdater.getDelay());
	}
	
	public long getCurrentFrame() {
		return _currentFrame;
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
		if (_initLoop != null) {
			if (_currentFrame++ == 0)
				_initLoop.run();
		}
		if (_drawLoop != null) {
			_drawLoop.run();
		}
		if (_bufferData == null || _bufferData.gz == null)
			return;
		_bufferData.gz.setColor(Color.black);
		_bufferData.gz.fillRect(0, 0, _bufferData.w, _bufferData.h);
		g.setColor(new Color(_clearColor.rgb()));
		g.fillRect(0, 0, _bufferData.w, _bufferData.h);
		_gl.draw(_bufferData);
	}

	public void recreateBuffer() {
		_isSubjectToCreation.set(true);
		while (_isInRender.get()) {
			
		}
		int w = (int) (getWidth() * _quality);
		if (w == 0)
			w++;
		int h = (int) (getHeight() * _quality);
		if (h == 0)
			h++;
		_buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		_bufferData = new ImageData();
		_bufferData.w = _buffer.getWidth();
		_bufferData.h = _buffer.getHeight();
		_bufferData.g = _buffer.getGraphics();
		_bufferData.data = ((DataBufferInt) _buffer.getRaster().getDataBuffer())
				.getData();
		_depth = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		_bufferData.zbuffer = ((DataBufferInt) _depth.getRaster()
				.getDataBuffer()).getData();
		_bufferData.gz = _depth.getGraphics();
		_isSubjectToCreation.set(false);
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
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		else if (_quality > 1)
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_lastGoodImage, 0, 0, getWidth(), getHeight(), null);
		if (isFPSVisible()) {
			g.setColor(Color.white);
			g.drawString("FPS:" + _lastFramesDrawn, 0, 10);
			g.drawString("Frame time: " + _frameTime + " ms", 0, 20);
			g.drawString("Real frame time: " + _realFrameTime + " ms", 0, 30);
			g.drawString("Triangles hidden by fog: " + _gl.getTrianglesHiddenByFog(), 0, 40);
			g.drawString("Triangles hidden by distance: " + _gl.getTrianglesHiddenByDistance(), 0, 50);
		}
	}
}
