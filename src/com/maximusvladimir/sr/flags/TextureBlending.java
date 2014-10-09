package com.maximusvladimir.sr.flags;

public enum TextureBlending {
	/**
	 * Lets the texture blend with the color of the pixel if the texture wasn't there.
	 * Example:
	 * 		Lets say the color at a point on a triangle is "5".
	 * 		Then lets say the color of the texture relevant to that texture at that point is "20".
	 * 		BlendWithColor takes "(5+20) / 2", and renders that color.
	 */
	BlendWithColor,
	/**
	 * Strictly uses the texture to get the color. Faster than BlendWithColor.
	 * Example:
	 * 		Lets say the color at a point on a triangle is "5".
	 * 		Then lets say the color of the texture relevant to that texture at that point is "20".
	 * 		JustTexture takes "20", and renders that color.
	 */
	JustTexture
}
