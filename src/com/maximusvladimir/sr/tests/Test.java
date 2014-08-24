package com.maximusvladimir.sr.tests;

import javax.swing.JFrame;

import com.maximusvladimir.sr.Component3D;
import com.maximusvladimir.sr.GL;
import com.maximusvladimir.sr.Point3D;
import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.flags.DepthMode;
import com.maximusvladimir.sr.flags.PolygonMode;
import com.maximusvladimir.sr.math.Matrix;

public class Test extends JFrame {
	private static final long serialVersionUID = 4571898300333512252L;

	public static void main(String[] args) {
		new Test();
	}

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

		Matrix viewMatrix = new Matrix();
		viewMatrix.setToLookAt(new Point3D(0, 0, -5), new Point3D(0, 1, 0),
				Point3D.Up);
		Matrix projectionMatrix = new Matrix();
		projectionMatrix.setToPerspective(0.78f, (float) 800 / 600.0f, 0.01f,
				10.0f);
		c.getGL().setViewMatrix(viewMatrix);
		c.getGL().setProjectionMatrix(projectionMatrix);
		c.getGL().setPolygonMode(PolygonMode.Fill);
		c.getGL().setDepthMode(DepthMode.PerPixel);

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
				gl.color(255, 0, 0);
				gl.vertex(1, 1, s);
				gl.color(0, 255, 0);
				gl.vertex(-1, 1, s2);
				gl.color(0, 0, 255);
				gl.vertex(0, -4, 10);
				
				gl.color(255, 0, 255);
				gl.vertex(4, 1, 16);
				gl.color(255, 255, 0);
				gl.vertex(6, 1, 16);
				gl.color(0, 255, 255);
				gl.vertex(5, -1, 16);
				
				gl.color(RGB.getPresetRandomColor());
				gl.vertex(-4, 1, 26);
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
