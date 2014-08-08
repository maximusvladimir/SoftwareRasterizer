package com.maximusvladimir.sr.math;

import java.awt.Graphics;

import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.RGBA;
import com.maximusvladimir.sr.flags.DepthMode;

public class ImageData {
	public int[] data;
	public int[] zbuffer;
	public int w,h;
	public Graphics g;
	public Graphics gz;
	public RGB lastGoodColor;
	public float znear;
	public float zfar;
	public DepthMode depthMode;
	public void setPixel(int x, int y, RGB c) {
		setPixel(x,y,0,c);
	}
	public void setPixel(int x, int y,int z, RGB c) {
		//System.out.println(z);
		int div = y * w + x;
		if (data != null && div > 0 && div < data.length - 1) {
			//c = RGB.lerp(RGB.Black,c,z/255.0f);
			if (c == null) {
				if (lastGoodColor.isTransparent())
					data[div] = RGB.lerp(lastGoodColor, new RGB(data[div]), ((RGBA)lastGoodColor).a() / 255.0f).rgb();
				else
					data[div] = lastGoodColor.rgb();
			} else {
				if (c.isTransparent())
					data[div] = RGBA.lerp(c, new RGB(data[div]), 1-(((RGBA)c).a() / 255.0f)).rgb();
				else
					data[div] = c.rgb();
				lastGoodColor = c;
			}
			if (zbuffer != null)
				zbuffer[div] = new RGB(z,z,z).rgb();
		}
	}
}
