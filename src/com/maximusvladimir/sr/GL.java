package com.maximusvladimir.sr;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.maximusvladimir.sr.flags.PolygonMode;
import com.maximusvladimir.sr.math.Display;
import com.maximusvladimir.sr.math.ImageData;
import com.maximusvladimir.sr.math.Matrix;
import com.maximusvladimir.sr.math.Triangle;

public class GL {
	private ArrayList<Operation> _operations = new ArrayList<Operation>();
	private ArrayList<Triangle> _triangles = new ArrayList<Triangle>();
	private ArrayList<DisplayList> _lists = new ArrayList<DisplayList>();
	private PolygonMode _polyMode = PolygonMode.Fill;
	private Matrix _viewMatrix = new Matrix();
	private Matrix _projectionMatrix = new Matrix();
	private int _pointSize = 3;

	GL() {

	}

	void draw(ImageData img) {
		img.znear = getProjectionMatrix().Znear;
		img.zfar = getProjectionMatrix().Zfar;
		img.lastGoodColor = RGB.White;
		Display.convertOperationsToTriangles(_operations, _triangles);
		_operations.clear();
		for (int i = 0; i < _lists.size(); i++) {
			_triangles.addAll(_lists.get(i).getData());
		}
		for (int i = 0; i < _triangles.size(); i++) {
			Triangle t = _triangles.get(i);
			Matrix mvp = t.mvp;
			if (t.list != null)
				mvp = Matrix.mul(
						Matrix.mul(t.list.getModelMatrix(), getViewMatrix()),
						getProjectionMatrix());
			Point3D p1 = t.p1.convertTo2D(mvp, img.w, img.h);
			Point3D p2 = t.p2.convertTo2D(mvp, img.w, img.h);
			Point3D p3 = t.p3.convertTo2D(mvp, img.w, img.h);
			if (_polyMode == PolygonMode.Fill) {
				Graphics g = img.g;
				g.setColor(img.lastGoodColor.asJavaAwtColor());
				if (t.c1 != null) {
					g.setColor(new Color(t.c1.rgb()));
					img.lastGoodColor = t.c1;
				}
				/*java.awt.Polygon p = new java.awt.Polygon();
				p.addPoint((int) p1.x, (int) p1.y);
				p.addPoint((int) p2.x, (int) p2.y);
				p.addPoint((int) p3.x, (int) p3.y);
				g.fillPolygon(p);*/
				Display.DrawTriangle(img, p1, p2, p3, t.c1,t.c2,t.c3);
				_lists.clear();
			} else if (_polyMode == PolygonMode.Line) {
				Display.drawLine(img, p1, p2, t.c1, t.c2);
				Display.drawLine(img, p3, p2, t.c3, t.c2);
				Display.drawLine(img, p1, p3, t.c1, t.c3);
			} else if (_polyMode == PolygonMode.Point) {
				Display.drawPoint(img, _pointSize, p1, t.c1);
				Display.drawPoint(img, _pointSize, p2, t.c2);
				Display.drawPoint(img, _pointSize, p3, t.c3);
			} else if (_polyMode == PolygonMode.LerpLine) {
				Display.drawLerpLine(img, p1, p2, t.c1, t.c2);
				Display.drawLerpLine(img, p2, p3, t.c2, t.c3);
				Display.drawLerpLine(img, p1, p3, t.c1, t.c3);
			}
		}
	}
	
	public void setPointSize(int s) {
		_pointSize = s;
	}
	
	public int getPointSize() {
		return _pointSize;
	}

	public void setViewMatrix(Matrix viewMatrix) {
		_viewMatrix = viewMatrix;
	}

	public Matrix getViewMatrix() {
		return _viewMatrix;
	}

	public void setProjectionMatrix(Matrix projectionMatrix) {
		_projectionMatrix = projectionMatrix;
	}

	public Matrix getProjectionMatrix() {
		return _projectionMatrix;
	}

	public void setPolygonMode(PolygonMode mode) {
		_polyMode = mode;
	}

	public PolygonMode getPolygonMode() {
		return _polyMode;
	}

	public void modelMatrix(Matrix model) {
		Matrix mvp = Matrix.mul(Matrix.mul(model, getViewMatrix()),
				getProjectionMatrix());
		_operations.add(mvp);
	}

	public void normal(float x, float y, float z) {
		normal(new Normal(x, y, z));
	}

	public void normal(Normal point) {
		_operations.add(point);
	}

	public void texCoord(float u, float v) {
		texCoord(new TextureCoord(u, v));
	}

	public void texCoord(TextureCoord tc) {
		_operations.add(tc);
	}

	public void color(int r, int g, int b) {
		color(new RGB(r, g, b));
	}
	
	public void color(int r, int g, int b, int a) {
		color(new RGBA(r, g, b, a));
	}
	
	public void color(RGBA rgba) {
		_operations.add(rgba);
	}

	public void color(RGB rgb) {
		_operations.add(rgb);
	}

	public void vertex(float x, float y, float z) {
		vertex(new Point3D(x, y, z));
	}

	public void vertex(Point3D point) {
		_operations.add(point);
	}

	public void callList(DisplayList list) {
		_operations.add(list);
	}
}
