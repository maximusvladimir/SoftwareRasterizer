package com.maximusvladimir.sr.ext;

import com.maximusvladimir.sr.Normal;
import com.maximusvladimir.sr.Point3D;
import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.Texture;
import com.maximusvladimir.sr.TextureCoord;
import com.maximusvladimir.sr.math.Matrix;

public class BasicTextureShader extends Shader<ShaderResult> {
	// Creating a local variable helps prevent the VM from creating too many objects.
	private ShaderResult result = new ShaderResult();
	public ShaderResult vertex(Matrix mvp, Point3D vertex, RGB color, Normal normal,
			TextureCoord coord) {
		result.position = vertex.convertTo2D(mvp, mvp.Width, mvp.Height);
		result.textureCoordinate = coord;
		return result;
	}

	public RGB pixel(ShaderResult vertexOutput, Texture texture) {
		return null;
	}

}
