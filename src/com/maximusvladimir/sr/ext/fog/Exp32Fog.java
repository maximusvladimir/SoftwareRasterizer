package com.maximusvladimir.sr.ext.fog;

public class Exp32Fog extends ExpFog {
	public float calculateFog(float z) {
		return (float)Math.pow(Math.E, -(getDensity() * z * Math.sqrt(z)));
	}
}
