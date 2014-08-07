package com.maximusvladimir.sr;

public class TextureCoord extends Operation {
	private float _u;
	private float _v;
	public TextureCoord() {
		this(0,0);
	}
	
	public TextureCoord(float u, float v) {
		id = 2;
		_u = u;
		_v = v;
	}
	
	public float u() {
		return _u;
	}
	
	public float _v() {
		return _v;
	}
	
	public void u(float u) {
		_u = u;
	}
	
	public void v(float v) {
		_v = v;
	}
}
