package com.maximusvladimir.sr;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.maximusvladimir.sr.flags.DepthMode;
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
	private DepthMode _depthMode = DepthMode.PerPixel;
	private float _fogBegin = 0;
	private float _fogEnd = 0;
	private RGB _fogColor = RGB.Gray;
	private boolean _fogEnabled = false;
	private long _threadId;
	
	GL(long threadId) {
		_threadId = threadId;
	}

	
	private VertexData _vdStruct1 = new VertexData();
	private VertexData _vdStruct2 = new VertexData();
	private VertexData _vdStruct3 = new VertexData();
	void draw(ImageData img) {
		img.znear = getProjectionMatrix().Znear;
		img.zfar = getProjectionMatrix().Zfar;
		img.lastGoodColor = RGB.White;
		img.depthMode = getDepthMode();
		Display.convertOperationsToTriangles(_operations, _triangles);
		_operations.clear();
		ArrayList<java.awt.Polygon> _depthPolygons = new ArrayList<java.awt.Polygon>();
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
				float z = 0;
				if (getDepthMode() == DepthMode.PerUnit) {
					z = (p1.z + p2.z + p3.z) * 0.3333333333333333333f;
				}
				_vdStruct1.p = p1;
				_vdStruct1.c = t.c1;
				_vdStruct2.p = p2;
				_vdStruct2.c = t.c2;
				_vdStruct3.p = p3;
				_vdStruct3.c = t.c3;
				//Display.DrawTriangle(img, new Point3I(p1), new Point3I(p2), new Point3I(p3), t.c1,t.c2,t.c3);
				Display.drawTriangle(img, _vdStruct1,_vdStruct2,_vdStruct3);
				if (getDepthMode() == DepthMode.PerUnit) {
					int c = Display.calculateDepth(img, z);
					img.gz.setColor(new RGB(c,c,c).asJavaAwtColor());
					java.awt.Polygon p = new java.awt.Polygon();
					p.addPoint((int) p1.x, (int) p1.y);
					p.addPoint((int) p2.x, (int) p2.y);
					p.addPoint((int) p3.x, (int) p3.y);
					img.gz.fillPolygon(p);
				}
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
	
	private long getGLThread() {
		return _threadId;
	}
	
	public boolean isOnGLThread() {
		return Thread.currentThread().getId() == getGLThread();
	}
	
	private void checkThrowBadThread() {
		if (!isOnGLThread())
			throw new RuntimeException("Attempting to access GL functions on wrong thread. Please place all drawing code inside the draw loop.");
	}
	
	public void setFogBegin(float begin) {
		checkThrowBadThread();
		_fogBegin = begin;
	}
	
	public float getFogBegin() {
		return _fogBegin;
	}
	
	public void setFogEnd(float end) {
		checkThrowBadThread();
		_fogEnd = end;
	}
	
	public float getFogEnd() {
		return _fogEnd;
	}
	
	public void setFogColor(RGB color) {
		checkThrowBadThread();
		_fogColor = color;
	}
	
	public RGB getFogColor() {
		return _fogColor;
	}
	
	public void setDepthMode(DepthMode mode) {
		checkThrowBadThread();
		_depthMode = mode;
	}
	
	public DepthMode getDepthMode() {
		return _depthMode;
	}
	
	public void setPointSize(int s) {
		checkThrowBadThread();
		_pointSize = s;
	}
	
	public int getPointSize() {
		return _pointSize;
	}

	public void setViewMatrix(Matrix viewMatrix) {
		checkThrowBadThread();
		_viewMatrix = viewMatrix;
	}

	public Matrix getViewMatrix() {
		return _viewMatrix;
	}

	public void setProjectionMatrix(Matrix projectionMatrix) {
		checkThrowBadThread();
		_projectionMatrix = projectionMatrix;
	}

	public Matrix getProjectionMatrix() {
		return _projectionMatrix;
	}

	public void setPolygonMode(PolygonMode mode) {
		checkThrowBadThread();
		_polyMode = mode;
	}

	public PolygonMode getPolygonMode() {
		return _polyMode;
	}

	public void modelMatrix(Matrix model) {
		checkThrowBadThread();
		Matrix mvp = Matrix.mul(Matrix.mul(model, getViewMatrix()),
				getProjectionMatrix());
		_operations.add(mvp);
	}

	public void normal(float x, float y, float z) {
		normal(new Normal(x, y, z));
	}

	public void normal(Normal point) {
		checkThrowBadThread();
		_operations.add(point);
	}

	public void texCoord(float u, float v) {
		texCoord(new TextureCoord(u, v));
	}

	public void texCoord(TextureCoord tc) {
		checkThrowBadThread();
		_operations.add(tc);
	}

	public void color(int r, int g, int b) {
		color(new RGB(r, g, b));
	}
	
	public void color(int r, int g, int b, int a) {
		color(new RGBA(r, g, b, a));
	}
	
	public void color(RGBA rgba) {
		checkThrowBadThread();
		_operations.add(rgba);
	}

	public void color(RGB rgb) {
		checkThrowBadThread();
		_operations.add(rgb);
	}

	public void vertex(float x, float y, float z) {
		vertex(new Point3D(x, y, z));
	}

	public void vertex(Point3D point) {
		checkThrowBadThread();
		_operations.add(point);
	}

	public void callList(DisplayList list) {
		checkThrowBadThread();
		_operations.add(list);
	}
}
