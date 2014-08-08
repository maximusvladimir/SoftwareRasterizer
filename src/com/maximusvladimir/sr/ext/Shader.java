package com.maximusvladimir.sr.ext;

import com.maximusvladimir.sr.Normal;
import com.maximusvladimir.sr.Point3D;
import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.Texture;
import com.maximusvladimir.sr.TextureCoord;
import com.maximusvladimir.sr.math.Matrix;

public abstract class Shader<T> {
	public Shader() {
		
	}
	
	public abstract T vertex(Matrix mvp, Point3D vertex, RGB color, Normal normal, TextureCoord coord);
	
	public abstract RGB pixel(T vertexOutput, Texture texture);
}
