package com.maximusvladimir.sr;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.maximusvladimir.sr.math.Display;

public class RGB extends Operation {
	public static final RGB AliceBlue = new RGB(240, 248, 255);
	public static final RGB AntiqueWhite = new RGB(250, 235, 215);
	public static final RGB Aqua = new RGB(0, 255, 255);
	public static final RGB Aquamarine = new RGB(127, 255, 212);
	public static final RGB Azure = new RGB(240, 255, 255);
	public static final RGB Beige = new RGB(245, 245, 220);
	public static final RGB Bisque = new RGB(255, 228, 196);
	public static final RGB Black = new RGB(0, 0, 0);
	public static final RGB BlanchedAlmond = new RGB(255, 235, 205);
	public static final RGB Blue = new RGB(0, 0, 255);
	public static final RGB BlueViolet = new RGB(138, 43, 226);
	public static final RGB Brown = new RGB(165, 42, 42);
	public static final RGB BurlyWood = new RGB(222, 184, 135);
	public static final RGB CadetBlue = new RGB(95, 158, 160);
	public static final RGB Chartreuse = new RGB(127, 255, 0);
	public static final RGB Chocolate = new RGB(210, 105, 30);
	public static final RGB Coral = new RGB(255, 127, 80);
	public static final RGB CornflowerBlue = new RGB(100, 149, 255);
	public static final RGB Cornsilk = new RGB(255, 248, 220);
	public static final RGB Crimson = new RGB(220, 20, 60);
	public static final RGB Cyan = new RGB(0, 255, 255);
	public static final RGB DarkBlue = new RGB(0, 0, 139);
	public static final RGB DarkCyan = new RGB(0, 139, 139);
	public static final RGB DarkGoldenrod = new RGB(184, 134, 11);
	public static final RGB DarkGray = new RGB(169, 169, 169);
	public static final RGB DarkGreen = new RGB(0, 100, 0);
	public static final RGB DarkKhaki = new RGB(189, 183, 107);
	public static final RGB DarkMagenta = new RGB(139, 0, 139);
	public static final RGB DarkOliveGreen = new RGB(85, 107, 47);
	public static final RGB DarkOrange = new RGB(255, 140, 0);
	public static final RGB DarkOrchid = new RGB(153, 50, 204);
	public static final RGB DarkRed = new RGB(139, 0, 0);
	public static final RGB DarkSalmon = new RGB(233, 150, 122);
	public static final RGB DarkSeaGreen = new RGB(143, 188, 143);
	public static final RGB DarkSlateBlue = new RGB(72, 61, 139);
	public static final RGB DarkSlateGray = new RGB(47, 79, 79);
	public static final RGB DarkTurquoise = new RGB(0, 206, 209);
	public static final RGB DarkViolet = new RGB(148, 0, 211);
	public static final RGB DeepPink = new RGB(255, 20, 147);
	public static final RGB DeepSkyBlue = new RGB(0, 191, 255);
	public static final RGB DimGray = new RGB(105, 105, 105);
	public static final RGB DodgerBlue = new RGB(30, 144, 255);
	public static final RGB FireBrick = new RGB(178, 34, 34);
	public static final RGB FloralWhite = new RGB(255, 250, 240);
	public static final RGB ForestGreen = new RGB(34, 139, 34);
	public static final RGB Fuchsia = new RGB(255, 0, 255);
	public static final RGB Gainsboro = new RGB(220, 220, 220);
	public static final RGB GhostWhite = new RGB(248, 248, 255);
	public static final RGB Gold = new RGB(255, 215, 0);
	public static final RGB Goldenrod = new RGB(218, 165, 32);
	public static final RGB Gray = new RGB(128, 128, 128);
	public static final RGB Green = new RGB(0, 128, 0);
	public static final RGB GreenYellow = new RGB(173, 255, 47);
	public static final RGB Honeydew = new RGB(240, 255, 240);
	public static final RGB HotPink = new RGB(255, 105, 180);
	public static final RGB IndianRed = new RGB(205, 92, 92);
	public static final RGB Indigo = new RGB(75, 0, 130);
	public static final RGB Ivory = new RGB(255, 255, 240);
	public static final RGB Khaki = new RGB(240, 230, 140);
	public static final RGB Lavender = new RGB(230, 230, 250);
	public static final RGB LavenderBlush = new RGB(255, 240, 245);
	public static final RGB LawnGreen = new RGB(124, 252, 0);
	public static final RGB LemonChiffon = new RGB(255, 250, 205);
	public static final RGB LightBlue = new RGB(173, 216, 230);
	public static final RGB LightCoral = new RGB(240, 128, 128);
	public static final RGB LightCyan = new RGB(224, 255, 255);
	public static final RGB LightGoldenrodYellow = new RGB(250, 250, 210);
	public static final RGB LightGreen = new RGB(144, 238, 144);
	public static final RGB LightGrey = new RGB(211, 211, 211);
	public static final RGB LightPink = new RGB(255, 182, 193);
	public static final RGB LightSalmon = new RGB(255, 160, 122);
	public static final RGB LightSeaGreen = new RGB(32, 178, 170);
	public static final RGB LightSkyBlue = new RGB(135, 206, 250);
	public static final RGB LightSlateGray = new RGB(119, 136, 153);
	public static final RGB LightSteelBlue = new RGB(176, 196, 222);
	public static final RGB LightYellow = new RGB(255, 255, 224);
	public static final RGB Lime = new RGB(0, 255, 0);
	public static final RGB LimeGreen = new RGB(50, 205, 50);
	public static final RGB Linen = new RGB(250, 240, 230);
	public static final RGB Magenta = new RGB(255, 0, 255);
	public static final RGB Maroon = new RGB(128, 0, 0);
	public static final RGB MediumAquamarine = new RGB(102, 205, 170);
	public static final RGB MediumBlue = new RGB(0, 0, 205);
	public static final RGB MediumOrchid = new RGB(186, 85, 211);
	public static final RGB MediumPurple = new RGB(147, 112, 219);
	public static final RGB MediumSeaGreen = new RGB(60, 179, 113);
	public static final RGB MediumSlateBlue = new RGB(123, 104, 238);
	public static final RGB MediumSpringGreen = new RGB(0, 250, 154);
	public static final RGB MediumTurquoise = new RGB(72, 209, 204);
	public static final RGB MediumVioletRed = new RGB(199, 21, 133);
	public static final RGB MidnightBlue = new RGB(25, 25, 112);
	public static final RGB MintCream = new RGB(245, 255, 250);
	public static final RGB MistyRose = new RGB(255, 228, 225);
	public static final RGB Moccasin = new RGB(255, 228, 181);
	public static final RGB NavajoWhite = new RGB(255, 222, 173);
	public static final RGB Navy = new RGB(0, 0, 128);
	public static final RGB OldLace = new RGB(253, 245, 230);
	public static final RGB Olive = new RGB(128, 128, 0);
	public static final RGB OliveDrab = new RGB(107, 142, 35);
	public static final RGB Orange = new RGB(255, 165, 0);
	public static final RGB OrangeRed = new RGB(255, 69, 0);
	public static final RGB Orchid = new RGB(218, 112, 214);
	public static final RGB PaleGoldenrod = new RGB(238, 232, 170);
	public static final RGB PaleGreen = new RGB(152, 251, 152);
	public static final RGB PaleTurquoise = new RGB(175, 238, 238);
	public static final RGB PaleVioletRed = new RGB(219, 112, 147);
	public static final RGB PapayaWhip = new RGB(255, 239, 213);
	public static final RGB PeachPuff = new RGB(255, 218, 185);
	public static final RGB Peru = new RGB(205, 133, 63);
	public static final RGB Pink = new RGB(255, 192, 203);
	public static final RGB Plum = new RGB(221, 160, 221);
	public static final RGB PowderBlue = new RGB(176, 224, 230);
	public static final RGB Purple = new RGB(128, 0, 128);
	public static final RGB Red = new RGB(255, 0, 0);
	public static final RGB RosyBrown = new RGB(188, 143, 143);
	public static final RGB RoyalBlue = new RGB(65, 105, 225);
	public static final RGB SaddleBrown = new RGB(139, 69, 19);
	public static final RGB Salmon = new RGB(250, 128, 114);
	public static final RGB SandyBrown = new RGB(244, 164, 96);
	public static final RGB SeaGreen = new RGB(46, 139, 87);
	public static final RGB Seashell = new RGB(255, 245, 238);
	public static final RGB Sienna = new RGB(160, 82, 45);
	public static final RGB Silver = new RGB(192, 192, 192);
	public static final RGB SkyBlue = new RGB(135, 206, 235);
	public static final RGB SlateBlue = new RGB(106, 90, 205);
	public static final RGB SlateGray = new RGB(112, 128, 144);
	public static final RGB Snow = new RGB(255, 250, 250);
	public static final RGB SpringGreen = new RGB(0, 255, 127);
	public static final RGB SteelBlue = new RGB(70, 130, 180);
	public static final RGB Tan = new RGB(210, 180, 140);
	public static final RGB Teal = new RGB(0, 128, 128);
	public static final RGB Thistle = new RGB(216, 191, 216);
	public static final RGB Tomato = new RGB(255, 99, 71);
	public static final RGB Turquoise = new RGB(64, 224, 208);
	public static final RGB Violet = new RGB(238, 130, 238);
	public static final RGB Wheat = new RGB(245, 222, 179);
	public static final RGB White = new RGB(255, 255, 255);
	public static final RGB WhiteSmoke = new RGB(245, 245, 245);
	public static final RGB Yellow = new RGB(255, 255, 0);
	public static final RGB YellowGreen = new RGB(154, 205, 50);
	protected int rgb = 0;
	protected boolean _isTransparent = false;

	public RGB() {
		id = 1;
	}

	public RGB(int rgb) {
		id = 1;
		this.rgb = rgb;
	}

	public RGB(RGBA down) {
		this(down.r(), down.g(), down.b());
	}

	public RGB(int r, int g, int b) {
		id = 1;
		set(r,g,b);
	}
	
	public void set(int r,int g, int b) {
		rgb = (r << 16) | (g << 8) | b;
	}
	
	public void set(int rgb) {
		this.rgb = rgb;
	}

	private static ArrayList<RGB> _staticColors = null;
	public static RGB getPresetRandomColor() {
		if (_staticColors == null) {
			_staticColors = new ArrayList<RGB>();
			RGB context = new RGB();
			Field[] fields = context.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getType().equals(context.getClass())) {
					try {
						_staticColors.add((RGB) fields[i].get(context));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return _staticColors.get((int)((_staticColors.size()-1)*Math.random()));
	}

	public Color asJavaAwtColor() {
		return new Color(rgb());
	}

	public boolean isTransparent() {
		return _isTransparent;
	}

	public int r() {
		return (rgb >> 16) & 0xFF;
	}

	public int g() {
		return (rgb >> 8) & 0xFF;
	}

	public int b() {
		return rgb & 0xFF;
	}

	public int rgb() {
		return rgb;
	}

	public static RGB lerp(RGB c1, RGB c2, float amount) {
		if (c1 == null)
			c1 = RGB.White;
		if (c2 == null)
			c2 = RGB.White;
		int r = (int) Display.lerp(c1.r(), c2.r(), amount);
		int g = (int) Display.lerp(c1.g(), c2.g(), amount);
		int b = (int) Display.lerp(c1.b(), c2.b(), amount);
		return new RGB(r, g, b);
	}

	public static RGB add(RGB c1, RGB c2) {
		int r = c1.r() + c2.r();
		int g = c1.g() + c2.g();
		int b = c1.b() + c2.b();
		return new RGB(r, g, b);
	}

	public static RGB sub(RGB c1, RGB c2) {
		int r = c1.r() - c2.r();
		int g = c1.g() - c2.g();
		int b = c1.b() - c2.b();
		return new RGB(r, g, b);
	}

	public static RGB mul(RGB c, float a) {
		float r = c.r() * a;
		float g = c.g() * a;
		float b = c.b() * a;
		return new RGB((int) r, (int) g, (int) b);
	}

	public static RGB avg(RGB... c) {
		int r = 0;
		int g = 0;
		int b = 0;
		int l = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == null)
				continue;
			l++;
			r += c[i].r();
			g += c[i].g();
			b += c[i].b();
		}
		if (l == 0)
			return RGB.Black;
		return new RGB((int) (r / l), (int) (g / l), (int) (b / l));
	}

	public String toString() {
		return "(" + r() + "," + g() + "," + b() + ")";
	}
}
