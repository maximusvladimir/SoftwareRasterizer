package com.maximusvladimir.sr;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.maximusvladimir.sr.flags.TextureFilter;
import com.maximusvladimir.sr.flags.TextureWrap;

public class Texture {
	private BufferedImage _texture;
	private TextureFilter _filter = TextureFilter.Nearest;
	private TextureWrap _wrap = TextureWrap.Clamp;
	private int[] _textData;
	private int _w;
	private int _h;
	private float _unitX;
	private float _unitY;
	private int _lastGoodColor = RGB.White.rgb();
	public Texture() {
		this(null);
	}
	
	public Texture(BufferedImage tx) {
		_texture = tx;
		if (_texture != null) {
			_w = _texture.getWidth();
			_h = _texture.getHeight();
			_unitX = 1.0f / _w;
			_unitY = 1.0f / _h;
			if (_texture.getType() == BufferedImage.TYPE_INT_RGB)
				_textData = ((DataBufferInt)tx.getRaster().getDataBuffer()).getData();
		}
	}
	
	void delete() {
		_textData = null;
		_texture.flush();
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
	
	RGB lookup(TextureCoord coord) {
		return lookup(coord.u(),coord.v());
	}
	
	public RGB lookup(float u, float v) {
		if (_texture == null)
			return RGB.Black;
		if (getTextureFilter() == TextureFilter.Linear) {
			RGB sample = internalLookup(u,v);
			RGB sample1 = internalLookup(u - _unitX,v);
			RGB sample2 = internalLookup(u + _unitX,v);
			RGB sample3 = internalLookup(u,v - _unitY);
			RGB sample4 = internalLookup(u,v + _unitY);
			RGB avg = RGB.avg(sample,sample1,sample2,sample3,sample4);
			return avg;
		}
		else{
			RGB c = internalLookup(u,v);
			if (c == null)
				c = RGB.Black;
			return c;
		}
	}
	
	private RGB internalLookup(float u, float v) {
		int pixx = (int)(u * (_w));
		int pixy = (int)(v * (_h));
		//System.out.println(pixx+","+pixy+","+_w);
		if (pixx > _w-1) {
			if (getTextureWrap() == TextureWrap.Clamp)
				return null;
			else
				pixx = (pixx % (_w));
		}
		if (pixy > _h-1) {
			if (getTextureWrap() == TextureWrap.Clamp)
				return null;
			else
				pixy = (pixy % (_h));
		}
		//System.out.println(pixx+","+pixy);
		if (_textData != null) {
			int det = (int)pixy * _texture.getWidth() + (int)pixx;
			if (det >= 0 && det < _textData.length) {
				int c = _textData[det];
				_lastGoodColor = c;
				return new RGB(c);
			}
			else
				return new RGB(_lastGoodColor);
		}
		else
			return new RGB(_texture.getRGB((int)pixx, (int)pixy));
	}
}
