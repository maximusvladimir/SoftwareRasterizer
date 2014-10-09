package com.maximusvladimir.sr;

public class Tuple4 {
	public float x,y,z,w;
	public Tuple4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Point3D asPoint() {
		return new Point3D(x,y,z);
	}
}
