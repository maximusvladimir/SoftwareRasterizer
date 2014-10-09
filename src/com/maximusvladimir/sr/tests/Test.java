package com.maximusvladimir.sr.tests;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.maximusvladimir.sr.Component3D;
import com.maximusvladimir.sr.GL;
import com.maximusvladimir.sr.Point3D;
import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.Texture;
import com.maximusvladimir.sr.ext.fog.LinearFog;
import com.maximusvladimir.sr.flags.DepthMode;
import com.maximusvladimir.sr.flags.PolygonMode;
import com.maximusvladimir.sr.flags.TextureFilter;
import com.maximusvladimir.sr.flags.TextureWrap;
import com.maximusvladimir.sr.math.Matrix;

public class Test extends JFrame {
	private static final long serialVersionUID = 4571898300333512252L;

	public static void main(String[] args) {
		new Test();
	}
	private TestDepth depth = new TestDepth();
	
	float qual = 1.0f;
	float playerPos = -5;
	
	public Test() {
		setSize(800, 600);
		setTitle("Test");
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
				if (ke.getKeyCode() == KeyEvent.VK_9) {
					qual += 0.05f;
					if (qual > 2)
						qual = 2;
					c.setQuality(qual);
				}
				if (ke.getKeyCode() == KeyEvent.VK_8) {
					qual -= 0.05f;
					if (qual <= 0)
						qual = 0.05f;
					c.setQuality(qual);
				}
				if (ke.getKeyCode() == KeyEvent.VK_S) {
					playerPos -= 0.5f;
					c.getGL().getViewMatrix().setToLookAt(new Point3D(0, 0, playerPos), new Point3D(0, 1, 0),
						Point3D.Up);
				}
				if (ke.getKeyCode() == KeyEvent.VK_W) {
					playerPos += 0.5f;
					c.getGL().getViewMatrix().setToLookAt(new Point3D(0, 0, playerPos), new Point3D(0, 1, 0),
						Point3D.Up);
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
				viewMatrix.setToLookAt(new Point3D(0, 0, playerPos), new Point3D(0, 1, 0),
						Point3D.Up);
				Matrix projectionMatrix = new Matrix();
				projectionMatrix.setToPerspective(0.78f, (float) 800 / 600.0f, 0.01f,
						10.0f);
				c.getGL().setViewMatrix(viewMatrix);
				c.getGL().setProjectionMatrix(projectionMatrix);
				c.getGL().setPolygonMode(PolygonMode.Fill);
				c.getGL().setDepthMode(DepthMode.PerPixel);
				c.getGL().setFogEquation(new LinearFog());
				c.getGL().setFogEnabled(true);
				
				BufferedImage te = new BufferedImage(32,32,BufferedImage.TYPE_INT_RGB);
				Graphics g = te.getGraphics();
				g.setColor(java.awt.Color.white);
				g.fillRect(0, 0, te.getWidth()+1,te.getHeight()+1);
				for (int x = 0; x < te.getWidth();x++) {
					for (int y = x%2; y < te.getHeight();y+=2) {
						g.setColor(java.awt.Color.black);
						g.drawLine(x,y,x,y);
					}
				}
				
				//Texture t = new Texture(te);
				Texture t = null;
				try {
					t = new Texture(Test.class.getResource("dummy.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				t.setTransparentColor(new RGB(195,195,195));
				t.rotate90CW();
				t.setTextureFilter(TextureFilter.Linear);
				t.setTextureWrap(TextureWrap.Repeat);
				c.getGL().createTexture(t);
				c.getGL().setTexturesEnabled(true);
				
				c.getGL().createTexture(new Texture(te));
			}
		});
		
		c.setDepthRenderLoop(new Runnable() {
			public void run() {
				depth.setDepthBuffer(c.getDepthBuffer());
			}
		});
		
		c.setDrawLoop(new Runnable() {
			float t = 0;
			public void run() {
				GL gl = c.getGL();
				Matrix model = new Matrix();
				model.setToTranslation(Point3D.Zero);
				float s = (float)(Math.cos(t)) * 2;
				float s2 = (float)(Math.sin(t)) * 2;
				t += 0.04f;
				
				gl.modelMatrix(model);
				
				gl.bindTexture(0);
				gl.texCoord(0,0);
				gl.color(255, 0, 0);
				gl.vertex(1, 1, s);
				gl.color(0, 255, 0);
				gl.texCoord(0,20);
				gl.vertex(-1, 1, s2);
				gl.color(0, 0, 255);
				gl.texCoord(20,0);
				gl.vertex(0, -4, 10);

				gl.color(255, 0, 255);
				gl.vertex(4, -1, 16);
				gl.color(255, 255, 0);
				gl.vertex(6, -1, 16);
				gl.color(0, 255, 255);
				gl.vertex(5, 1, 16);
				
				gl.color(RGB.getPresetRandomColor());
				gl.vertex(-4, 1, 26);
				gl.texCoord(0,1);
				gl.color(RGB.getPresetRandomColor());
				gl.vertex(-6, 1, 26);
				gl.color(RGB.getPresetRandomColor());
				gl.vertex(-5, -1, 26);
				
				gl.color(RGB.Aqua);
				gl.vertex(0.5f,0.5f,1);
				gl.color(RGB.DarkGoldenrod);
				gl.vertex(-0.5f,0.5f,1);
				gl.color(RGB.CadetBlue);
				gl.vertex(0,-1,12);
			}
		});
	}
}
