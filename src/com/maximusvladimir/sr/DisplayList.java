package com.maximusvladimir.sr;

import java.util.ArrayList;

import com.maximusvladimir.sr.math.Display;
import com.maximusvladimir.sr.math.Matrix;
import com.maximusvladimir.sr.math.Triangle;

public class DisplayList extends Operation {
	private ArrayList<Operation> _operations = new ArrayList<Operation>();
	private ArrayList<Triangle> _triangles = new ArrayList<Triangle>();
	private Matrix _model;
	DisplayList() {
		id = 6;
	}
	
	public void setModelMatrix(Matrix model) {
		_model = model;
	}
	
	public Matrix getModelMatrix() {
		return _model;
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
	
	public ArrayList<Triangle> getData() {
		return _triangles;
	}
	
	public void build() {
		Display.convertOperationsToTriangles(null,_operations, _triangles);
		for (int i = 0; i < _triangles.size(); i++) {
			_triangles.get(i).list = this;
		}
		_operations.clear();
	}
}
