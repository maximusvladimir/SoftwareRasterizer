package com.maximusvladimir.sr;

import java.awt.image.BufferedImage;

import com.maximusvladimir.sr.flags.TextureFilter;
import com.maximusvladimir.sr.flags.TextureWrap;

public class Texture extends Operation {
	private BufferedImage _texture;
	private TextureFilter _filter = TextureFilter.Nearest;
	private TextureWrap _wrap = TextureWrap.Clamp;
	private int _w;
	private int _h;
	private float _unitX;
	private float _unitY;
	public Texture() {
		this(null);
	}
	
	public Texture(BufferedImage tx) {
		id = 4;
		_texture = tx;
		if (_texture != null) {
			_w = _texture.getWidth();
			_h = _texture.getHeight();
			_unitX = 1.0f / _w;
			_unitY = 1.0f / _h;
		}
	}
	
	public void setTextureFilter(TextureFilter filter) {
		_filter = filter;
	}
	
	public TextureFilter getTextureFilter() {
		return _filter;
	}
	
	public void setTextureWrap(TextureWrap wrap) {
		_wrap = wrap;
	}
	
	public TextureWrap getTextureWrap() {
		return _wrap;
	}
	
	RGB map(TextureCoord coord) {
		return map(coord.u(),coord._v());
	}
	
	RGB map(float u, float v) {
		if (_texture == null)
			return RGB.Black;
		if (getTextureFilter() == TextureFilter.Linear) {
			RGB sample = internalMap(u,v);
			RGB sample1 = internalMap(u - _unitX,v);
			RGB sample2 = internalMap(u + _unitX,v);
			RGB sample3 = internalMap(u,v - _unitY);
			RGB sample4 = internalMap(u,v + _unitY);
			RGB avg = RGB.avg(sample,sample1,sample2,sample3,sample4);
			return avg;
		}
		else{
			RGB c = internalMap(u,v);
			if (c == null)
				c = RGB.Black;
			return c;
		}
	}
	
	private RGB internalMap(float u, float v) {
		int pixx = (int)(u * _w);
		int pixy = (int)(v * _h);
		if (pixx > _w - 1) {
			if (getTextureWrap() == TextureWrap.Clamp)
				return null;
			else
				pixx = pixx % _w;
		}
		if (pixy > _h - 1) {
			if (getTextureWrap() == TextureWrap.Clamp)
				return null;
			else
				pixx = pixy % _h;
		}
		
		return new RGB(_texture.getRGB(pixx, pixy));
	}
}
