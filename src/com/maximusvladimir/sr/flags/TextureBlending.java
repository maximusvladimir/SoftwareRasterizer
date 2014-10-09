package com.maximusvladimir.sr.flags;

public enum TextureBlending {
	/**
	 * Lets the texture blend with the color of the pixel if the texture wasn't there.
	 */
	BlendWithColor,
	/**
	 * Strictly uses the texture to get the color. Faster than BlendWithColor.
	 */
	JustTexture
}
