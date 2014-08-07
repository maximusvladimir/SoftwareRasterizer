package com.maximusvladimir.sr;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.maximusvladimir.sr.flags.PolygonMode;
import com.maximusvladimir.sr.math.Display;
import com.maximusvladimir.sr.math.Triangle;

public class GL {
	private ArrayList<Operation> _operations = new ArrayList<Operation>();
	private ArrayList<Triangle> _triangles = new ArrayList<Triangle>();
	private ArrayList<DisplayList> _lists = new ArrayList<DisplayList>();
	private PolygonMode _polyMode = PolygonMode.Fill;
	GL() {
		
	}
	
	void draw(BufferedImage buffer) {
		if (_polyMode == PolygonMode.Fill) {
			Display.convertOperationsToTriangles(_operations, _triangles);
			_operations.clear();
			for (int i = 0; i < _lists.size(); i++) {
				_triangles.addAll(_lists.get(i).getData());
			}
			_lists.clear();
		}
	}
	
	public void setPolygonMode(PolygonMode mode) {
		_polyMode = mode;
	}
	
	public PolygonMode getPolygonMode() {
		return _polyMode;
	}
	
	public void normal(float x, float y, float z) {
		normal(new Normal(x,y,z));
	}
	
	public void normal(Normal point) {
		_operations.add(point);
	}
	
	public void texCoord(float u, float v) {
		texCoord(new TextureCoord(u,v));
	}
	
	public void texCoord(TextureCoord tc) {
		_operations.add(tc);
	}
	
	public void color(int r, int g, int b) {
		color(new RGB(r,g,b));
	}
	
	public void color(RGB rgb) {
		_operations.add(rgb);
	}
	
	public void vertex(float x, float y, float z) {
		vertex(new Point3D(x,y,z));
	}
	
	public void vertex(Point3D point) {
		_operations.add(point);
	}
	
	public void callList(DisplayList list) {
		_lists.add(list);
	}
}
