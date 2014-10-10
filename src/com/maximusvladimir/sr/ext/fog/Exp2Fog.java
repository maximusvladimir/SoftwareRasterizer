package com.maximusvladimir.sr.ext.fog;

public class Exp2Fog extends ExpFog {
	public float calculateFog(float z) {
		float n = getDensity() * z;
		return (float)Math.pow(Math.E, -(n * n));
	}
}
