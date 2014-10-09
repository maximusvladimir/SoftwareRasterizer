package com.maximusvladimir.sr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import com.maximusvladimir.sr.flags.TextureBlending;
import com.maximusvladimir.sr.flags.TextureFilter;
import com.maximusvladimir.sr.flags.TextureWrap;

public class Texture {
	private BufferedImage _texture;
	private TextureFilter _filter = TextureFilter.Nearest;
	private TextureWrap _wrap = TextureWrap.Clamp;
	private TextureBlending _blend = TextureBlending.JustTexture;
	private int[] _textData;
	private int _w;
	private int _h;
	private float _unitX;
	private float _unitY;
	private int _lastGoodColor = RGB.White.rgb();
	private RGB _transparencyKey = null;
	public Texture() {
		this((BufferedImage)null);
	}
	
	public Texture(File input) throws IOException {
		this(setRightType(ImageIO.read(input)));
	}
	
	public Texture(URL input) throws IOException {
		this(setRightType(ImageIO.read(input)));
	}
	
	public Texture(InputStream input) throws IOException {
		this(setRightType(ImageIO.read(input)));
	}
	
	public Texture(ImageInputStream input) throws IOException {
		this(setRightType(ImageIO.read(input)));
	}
	
	private static BufferedImage setRightType(BufferedImage b) {
		if (b != null) {
			BufferedImage cpy = new BufferedImage(b.getWidth(),b.getHeight(),BufferedImage.TYPE_INT_RGB);
			Graphics g = cpy.getGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, b.getWidth(),b.getHeight());
			g.drawImage(b, 0, 0, null);
			g.dispose();
			return cpy;
		}
		return null;
	}
	
	public Texture(BufferedImage tx) {
		_texture = tx;
		if (_texture != null) {
			_w = _texture.getWidth();
			_h = _texture.getHeight();
			_unitX = 1.0f / _w * 0.25f;
			_unitY = 1.0f / _h * 0.25f;
			if (_texture.getType() == BufferedImage.TYPE_INT_RGB)
				_textData = ((DataBufferInt)tx.getRaster().getDataBuffer()).getData();
		}
	}
	
	public void rotate90CW() {
		if (_textData != null) {
			int[] cpy = new int[_textData.length];
			System.arraycopy(_textData, 0, cpy, 0, cpy.length);
			for (int x = 0; x < _w; x++) {
				for (int y = 0; y < _h; y++) {
					_textData[y * _w + x] = cpy[(_w-x-1) * _h + y];
				}
			}
		}
		else {
			throw new RuntimeException("Unsupported operation.");
		}
	}
	
	public void rotate90CCW() {
		if (_textData != null) {
			int[] cpy = new int[_textData.length];
			System.arraycopy(_textData, 0, cpy, 0, cpy.length);
			for (int x = 0; x < _w; x++) {
				for (int y = 0; y < _h; y++) {
					_textData[x * _h + y] = cpy[y * _w + x];
				}
			}
		}
		else {
			throw new RuntimeException("Unsupported operation.");
		}
	}
	
	void delete() {
		_textData = null;
		_texture.flush();
	}
	
	public void setTransparentColor(RGB c) {
		_transparencyKey = c;
	}
	
	public RGB getTransparentColor() {
		return _transparencyKey;
	}
	
	public void setTextureBlending(TextureBlending blend) {
		_blend = blend;
	}
	
	public TextureBlending getTextureBlending() {
		return _blend;
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
			if (getTextureWrap() == TextureWrap.Clear)
				return null;
			else if (getTextureWrap() == TextureWrap.Repeat)
				pixx = (pixx % (_w));
			else
				pixx = _w-1;
		}
		if (pixy > _h-1) {
			if (getTextureWrap() == TextureWrap.Clear)
				return null;
			else if (getTextureWrap() == TextureWrap.Repeat)
				pixy = (pixy % (_h));
			else
				pixy = _h-1;
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
