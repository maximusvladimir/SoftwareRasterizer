package com.maximusvladimir.sr.flags;

public enum DepthMode {
	/**
	 * Turns off the depth buffer.
	 */
	Off,
	/**
	 * Uses per-pixel depth. Much slower than per-unit. Uses individual pixels to determine the depth.
	 */
	PerPixel,
	/**
	 * Uses per-unit depth. Much faster than per-pixel. Takes the average distance of the unit (triangle, point, etc.), to gather the depth.
	 */
	PerUnit
}
