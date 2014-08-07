package com.maximusvladimir.sr;

public class Normal extends Operation {
	private float _x, _y, _z;
	public Normal() {
		this(0,0,0);
	}
	
	public Normal(float x, float y, float z) {
		_x = x;
		_y = y;
		_z = z;
		id = 3;
	}
	
	public float x() {
		return _x;
	}
	
	public float y() {
		return _y;
	}
	
	public float z() {
		return _z;
	}
	
	public void x(float x) {
		_x = x;
	}
	
	public void y(float y) {
		_y = y;
	}
	
	public void z(float z) {
		_z = z;
	}
}
