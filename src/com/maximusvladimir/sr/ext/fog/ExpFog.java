package com.maximusvladimir.sr.ext.fog;

public class ExpFog implements IFogEquation {
	private float _density = 0.1f;
	public ExpFog() {
		
	}
	
	public ExpFog(float density) {
		_density = density;
	}
	
	public void setDensity(float density) {
		_density = density;
	}
	
	public float getDensity() {
		return _density;
	}
	
	public float calculateFog(float z) {
		return (float)Math.pow(Math.E, -getDensity() * z);
	}
}
