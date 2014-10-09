package com.maximusvladimir.sr.ext.fog;

public interface IFogEquation {
	/**
	 * Calculates the amount that fog should be interpolated between the
	 * original pixel color and the fog.
	 * @param z The z-depth of the pixel.
	 * @return A value between 0.0 and 1.0 representing the interpolation between
	 * fog and pixel color.
	 */
	public float calculateFog(float z);
}
