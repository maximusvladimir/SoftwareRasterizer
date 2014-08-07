package com.maximusvladimir.sr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

public class Component3D extends Component {
	private static final long serialVersionUID = 664343555845197174L;
	private BufferedImage _buffer;
	private Boolean _isRendering = (Boolean) false;
	private Object _lock = new Object();
	private long _lastFPSSample;
	private int _framesDrawn = 0;
	private int _lastFramesDrawn = 0;
	private boolean _showFPS = true;
	private Timer _logicUpdater;
	private RGB _clearColor = RGB.CornflowerBlue;
	private GL _gl;

	public Component3D() {
		this(false,30);
	}
	
	public Component3D(boolean allowClearBuffer, int targettedFPS) {
		setSize(100, 100);
		_gl = new GL();
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
					render(_buffer.getGraphics());
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
	}
	
	public void setTargettedFPS(int fps) {
		_logicUpdater.setDelay((int)(1000.0 / fps));
	}
	
	public int getTargettedFPS() {
		return (int)(1000.0 / _logicUpdater.getDelay());
	}

	public void setFPSVisible(boolean v) {
		_showFPS = v;
	}

	public boolean isFPSVisible() {
		return _showFPS;
	}

	private void render(Graphics g) {
		g.setColor(new Color(_clearColor.rgb()));
		g.fillRect(0, 0, getWidth(), getHeight());
		_gl.draw(_buffer);
	}

	public void recreateBuffer() {
		int w = getWidth();
		if (w == 0)
			w++;
		int h = getHeight();
		if (h == 0)
			h++;
		_buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	}

	public void paint(Graphics g) {
		if (System.currentTimeMillis() - _lastFPSSample >= 1000) {
			_lastFPSSample = System.currentTimeMillis();
			_lastFramesDrawn = _framesDrawn;
			_framesDrawn = 0;
		}

		if (!_isRendering)
			g.drawImage(_buffer, 0, 0, null);
		if (isFPSVisible()) {
			g.setColor(Color.white);
			g.drawString("FPS:" + _lastFramesDrawn, 0, 10);
		}
	}
}
