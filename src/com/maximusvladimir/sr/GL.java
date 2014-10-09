package com.maximusvladimir.sr;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import com.maximusvladimir.sr.ext.fog.IFogEquation;
import com.maximusvladimir.sr.flags.DepthMode;
import com.maximusvladimir.sr.flags.FogMode;
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
	private FogMode _fogMode = FogMode.NoFog;
	private boolean _texturingEnabled = false;
	private long _threadId;
	private IFogEquation _fogEquation;
	private int _currentTextureCounter = 0;
	private int _trianglesOccludedByFog = 0;
	private int _trianglesOccludedByDistance = 0;
	private HashMap<Integer,Texture> _textures = new HashMap<Integer,Texture>();
	
	GL(long threadId) {
		_threadId = threadId;
	}

	
	private VertexData _vdStruct1 = new VertexData();
	private VertexData _vdStruct2 = new VertexData();
	private VertexData _vdStruct3 = new VertexData();
	void draw(ImageData img) {
		img.gl = this;
		img.znear = getProjectionMatrix().Znear;
		img.zfar = getProjectionMatrix().Zfar;
		img.lastGoodColor = RGB.White;
		img.depthMode = getDepthMode();
		
		if (getFogMode() == FogMode.ClearBackground) {
			img.g.setColor(getFogColor().asJavaAwtColor());
			img.g.fillRect(0, 0, img.w, img.h);
		}
		
		Display.convertOperationsToTriangles(this,_operations, _triangles);
		_operations.clear();
		ArrayList<java.awt.Polygon> _depthPolygons = new ArrayList<java.awt.Polygon>();
		for (int i = 0; i < _lists.size(); i++) {
			_triangles.addAll(_lists.get(i).getData());
		}
		int occlFog = 0;
		int occlDis = 0;
		for (int i = 0; i < _triangles.size(); i++) {
			Triangle t = _triangles.get(i);
			Matrix mvp = t.mvp;
			if (t.list != null)
				mvp = Matrix.mul(
						Matrix.mul(t.list.getModelMatrix(), getViewMatrix()),
						getProjectionMatrix());
			Tuple4 p1 = t.p1.convertTo2D(mvp, img.w, img.h);
			Tuple4 p2 = t.p2.convertTo2D(mvp, img.w, img.h);
			Tuple4 p3 = t.p3.convertTo2D(mvp, img.w, img.h);
			Point3D pn1 = p1.asPoint();
			Point3D pn2 = p2.asPoint();
			Point3D pn3 = p3.asPoint();
			if (p1.w <= 0 && p2.w <= 0 && p3.w <= 0) {
				occlDis++;
				continue;
			}
			if (getFogMode() == FogMode.ClearBackground && getFogEquation() != null && getFogEquation().calculateFog(p1.w) >= 1 && getFogEquation().calculateFog(p2.w) >= 1 && getFogEquation().calculateFog(p3.w) >= 1) {
				occlFog++;
				continue;
		}
			if (_polyMode == PolygonMode.Fill) {
				Graphics g = img.g;
				g.setColor(img.lastGoodColor.asJavaAwtColor());
				if (t.c1 != null) {
					g.setColor(new Color(t.c1.rgb()));
					img.lastGoodColor = t.c1;
				}
				else {
					t.c1 = t.c2 = t.c3 = RGB.White;
				}
				if (t.tc1 == null)
					t.tc1 = TextureCoord.ZERO;
				if (t.tc2 == null)
					t.tc2 = TextureCoord.ZERO;
				if (t.tc3 == null)
					t.tc3 = TextureCoord.ZERO;
				float z = 0;
				if (getDepthMode() == DepthMode.PerUnit) {
					z = (p1.z + p2.z + p3.z) * 0.3333333333333333333f;
				}
				_vdStruct1.p = p1;
				_vdStruct1.c = t.c1;
				_vdStruct1.t = t.tc1;
				_vdStruct2.p = p2;
				_vdStruct2.c = t.c2;
				_vdStruct2.t = t.tc2;
				_vdStruct3.p = p3;
				_vdStruct3.c = t.c3;
				_vdStruct3.t = t.tc3;
				img.activeTex = t.texture;
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
				Display.drawLine(img, pn1, pn2, t.c1, t.c2);
				Display.drawLine(img, pn3, pn2, t.c3, t.c2);
				Display.drawLine(img, pn1, pn3, t.c1, t.c3);
			} else if (_polyMode == PolygonMode.Point) {
				Display.drawPoint(img, _pointSize, pn1, t.c1);
				Display.drawPoint(img, _pointSize, pn2, t.c2);
				Display.drawPoint(img, _pointSize, pn3, t.c3);
			} else if (_polyMode == PolygonMode.LerpLine) {
				Display.drawLerpLine(img, pn1, pn2, t.c1, t.c2);
				Display.drawLerpLine(img, pn2, pn3, t.c2, t.c3);
				Display.drawLerpLine(img, pn1, pn3, t.c1, t.c3);
			}
		}
		_trianglesOccludedByFog = occlFog;
		_trianglesOccludedByDistance = occlDis;
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
	
	public int createTexture(Texture t) {
		if (t == null)
			return -1;
		_textures.put(_currentTextureCounter, t);
		return _currentTextureCounter++;
	}
	
	public boolean deleteTexture(int id) {
		if (_textures.containsKey(id)) {
			Texture t = _textures.remove(id);
			try {
				t.delete();
			}
			catch (Throwable t2) {
				
			}
			return true;
		}
		return false;
	}
	
	public Texture getTexture(int id) {
		if (_textures.containsKey(id))
			return _textures.get(id);
		return null;
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
	
	public void setTexturesEnabled(boolean texturing) {
		checkThrowBadThread();
		_texturingEnabled = texturing;
	}
	
	public boolean isTexturingEnabled() {
		return _texturingEnabled;
	}
	
	public void setFogEquation(IFogEquation fogEquation) {
		checkThrowBadThread();
		_fogEquation = fogEquation;
	}
	
	public IFogEquation getFogEquation() {
		return _fogEquation;
	}
	
	public void setPointSize(int s) {
		checkThrowBadThread();
		_pointSize = s;
	}
	
	public int getPointSize() {
		return _pointSize;
	}
	
	public void setFogMode(FogMode mode) {
		checkThrowBadThread();
		_fogMode = mode;
	}
	
	public FogMode getFogMode() {
		return _fogMode;
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
	
	public int getTrianglesHiddenByFog() {
		return _trianglesOccludedByFog;
	}
	
	public int getTrianglesHiddenByDistance() {
		return _trianglesOccludedByDistance;
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
	
	public void bindTexture(int id) {
		checkThrowBadThread();
		_operations.add(new ActiveTexture(id));
	}

	public void callList(DisplayList list) {
		checkThrowBadThread();
		_operations.add(list);
	}
}
