package com.maximusvladimir.sr.tests;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class TestDepth extends JFrame {
	private static final long serialVersionUID = -1752797618473183749L;
	private BufferedImage _depth;
	public TestDepth() {
		setVisible(true);
		setSize(100,100);
	}
	
	public void setDepthBuffer(BufferedImage b) {
		_depth = b;
		if (_depth.getWidth() != getWidth() || _depth.getHeight() != getHeight()) {
			setSize(_depth.getWidth(),_depth.getHeight());
			setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - _depth.getWidth(),0);
		}
		repaint();
	}
	
	public void paint(Graphics g) {
		g.drawImage(_depth,0,0,null);
	}
}
