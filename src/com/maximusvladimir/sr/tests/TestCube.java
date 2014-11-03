package com.maximusvladimir.sr.tests;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import com.maximusvladimir.sr.Component3D;
import com.maximusvladimir.sr.GL;
import com.maximusvladimir.sr.Point3D;
import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.Texture;
import com.maximusvladimir.sr.ext.HitTestHelper;
import com.maximusvladimir.sr.ext.fog.LinearFog;
import com.maximusvladimir.sr.flags.DepthMode;
import com.maximusvladimir.sr.flags.FogMode;
import com.maximusvladimir.sr.flags.PolygonMode;
import com.maximusvladimir.sr.flags.TextureBlending;
import com.maximusvladimir.sr.flags.TextureFilter;
import com.maximusvladimir.sr.flags.TextureWrap;
import com.maximusvladimir.sr.math.Matrix;

public class TestCube extends JFrame {
	private static final long serialVersionUID = 4571898306436512252L;

	public static void main(String[] args) {
		new TestCube();
	}
	
	public TestCube() {
		setSize(800, 600);
		setTitle(getClass().getName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		final Component3D c = new Component3D();
		c.setSize(800, 600);
		c.setTargettedFPS(60);
		c.setQuality(1.0f);
		getContentPane().add(c);
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_F11) {
					if (c.isFullscreen())
						c.setFullscreen(false, 0, 0);
					else
						c.setFullscreen(true, 640,480);
				}
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent arg0) {
				c.getGL().getProjectionMatrix().setToPerspective(0.78f, ((float)c.getWidth()) / c.getHeight(), 0.01f,
						10.0f);
			}
		});
		
		c.setInitProc(new Runnable() {
			public void run() {
				Matrix viewMatrix = new Matrix();
				viewMatrix.setToLookAt(new Point3D(0,0,-10), new Point3D(0, 1, 0),
						Point3D.Up);
				Matrix projectionMatrix = new Matrix();
				projectionMatrix.setToPerspective(0.78f, (float) 800 / 600.0f, 0.01f,
						10.0f);
				c.getGL().setViewMatrix(viewMatrix);
				c.getGL().setProjectionMatrix(projectionMatrix);
				c.getGL().setPolygonMode(PolygonMode.Fill);
				c.getGL().setDepthMode(DepthMode.PerPixel);
				c.getGL().setFogMode(FogMode.NoFog);
				
				BufferedImage te = new BufferedImage(32,32,BufferedImage.TYPE_INT_RGB);
				Graphics g = te.getGraphics();
				g.setColor(java.awt.Color.white);
				g.fillRect(0, 0, te.getWidth()+1,te.getHeight()+1);
				g.setColor(java.awt.Color.black);
				for (int x = 0; x < te.getWidth();x++) {
					for (int y = x%2; y < te.getHeight();y+=2) {
						g.drawLine(x,y,x,y);
					}
				}
				
				c.getGL().setTexturesEnabled(true);
				Texture tex = new Texture(te);
				tex.setTextureWrap(TextureWrap.Repeat);
				//tex.setTextureBlending(TextureBlending.BlendWithColor);
				c.getGL().createTexture(tex);
			}
		});
		
		c.setDrawLoop(new Runnable() {
			private float delt = 0;
			public void run() {
				GL gl = c.getGL();
				Matrix model = new Matrix();
				delt += 0.05f;
				///model.setToTranslation(Point3D.Zero);
				model.setToRotation(0, 1, 0, delt);
				
				gl.modelMatrix(model);
				
				gl.bindTexture(0);
				gl.texCoord(0,0);
				gl.color(255, 0, 255);
				gl.vertex(-1, -1, 5);
				gl.color(255, 255, 0);
				gl.texCoord(0, 0.2f);
				gl.vertex(1, -1, 5);
				gl.color(0, 255, 255);
				gl.texCoord(0.2f,0.2f);
				gl.vertex(1, 1, 5);
			}
		});
	}
}
