package com.maximusvladimir.sr;

import java.awt.Color;

import com.maximusvladimir.sr.math.Display;

public class RGBA extends RGB {
	public static final RGBA AliceBlue = new RGBA(240, 248, 255);
	public static final RGBA AntiqueWhite = new RGBA(250, 235, 215);
	public static final RGBA Aqua = new RGBA(0, 255, 255);
	public static final RGBA Aquamarine = new RGBA(127, 255, 212);
	public static final RGBA Azure = new RGBA(240, 255, 255);
	public static final RGBA Beige = new RGBA(245, 245, 220);
	public static final RGBA Bisque = new RGBA(255, 228, 196);
	public static final RGBA Black = new RGBA(0,0,0);
	public static final RGBA BlanchedAlmond = new RGBA(255, 235, 205);
	public static final RGBA Blue = new RGBA(0, 0, 255);
	public static final RGBA BlueViolet = new RGBA(138, 43, 226);
	public static final RGBA Brown = new RGBA(165, 42, 42);
	public static final RGBA BurlyWood = new RGBA(222, 184, 135);
	public static final RGBA CadetBlue = new RGBA(95, 158, 160);
	public static final RGBA Chartreuse = new RGBA(127, 255, 0);
	public static final RGBA Chocolate = new RGBA(210, 105, 30);
	public static final RGBA Coral = new RGBA(255, 127, 80);
	public static final RGBA CornflowerBlue = new RGBA(100, 149, 255);
	public static final RGBA Cornsilk = new RGBA(255, 248, 220);
	public static final RGBA Crimson = new RGBA(220, 20, 60);
	public static final RGBA Cyan = new RGBA(0, 255, 255);
	public static final RGBA DarkBlue = new RGBA(0, 0, 139);
	public static final RGBA DarkCyan = new RGBA(0, 139, 139);
	public static final RGBA DarkGoldenrod = new RGBA(184, 134, 11);
	public static final RGBA DarkGray = new RGBA(169, 169, 169);
	public static final RGBA DarkGreen = new RGBA(0, 100, 0);
	public static final RGBA DarkKhaki = new RGBA(189, 183, 107);
	public static final RGBA DarkMagenta = new RGBA(139, 0, 139);
	public static final RGBA DarkOliveGreen = new RGBA(85, 107, 47);
	public static final RGBA DarkOrange = new RGBA(255, 140, 0);
	public static final RGBA DarkOrchid = new RGBA(153, 50, 204);
	public static final RGBA DarkRed = new RGBA(139, 0, 0);
	public static final RGBA DarkSalmon = new RGBA(233, 150, 122);
	public static final RGBA DarkSeaGreen = new RGBA(143, 188, 143);
	public static final RGBA DarkSlateBlue = new RGBA(72, 61, 139);
	public static final RGBA DarkSlateGray = new RGBA(47, 79, 79);
	public static final RGBA DarkTurquoise = new RGBA(0, 206, 209);
	public static final RGBA DarkViolet = new RGBA(148, 0, 211);
	public static final RGBA DeepPink = new RGBA(255, 20, 147);
	public static final RGBA DeepSkyBlue = new RGBA(0, 191, 255);
	public static final RGBA DimGray = new RGBA(105, 105, 105);
	public static final RGBA DodgerBlue = new RGBA(30, 144, 255);
	public static final RGBA FireBrick = new RGBA(178, 34, 34);
	public static final RGBA FloralWhite = new RGBA(255, 250, 240);
	public static final RGBA ForestGreen = new RGBA(34, 139, 34);
	public static final RGBA Fuchsia = new RGBA(255, 0, 255);
	public static final RGBA Gainsboro = new RGBA(220, 220, 220);
	public static final RGBA GhostWhite = new RGBA(248, 248, 255);
	public static final RGBA Gold = new RGBA(255, 215, 0);
	public static final RGBA Goldenrod = new RGBA(218, 165, 32);
	public static final RGBA Gray = new RGBA(128, 128, 128);
	public static final RGBA Green = new RGBA(0, 128, 0);
	public static final RGBA GreenYellow = new RGBA(173, 255, 47);
	public static final RGBA Honeydew = new RGBA(240, 255, 240);
	public static final RGBA HotPink = new RGBA(255, 105, 180);
	public static final RGBA IndianRed = new RGBA(205, 92, 92);
	public static final RGBA Indigo = new RGBA(75, 0, 130);
	public static final RGBA Ivory = new RGBA(255, 255, 240);
	public static final RGBA Khaki = new RGBA(240, 230, 140);
	public static final RGBA Lavender = new RGBA(230, 230, 250);
	public static final RGBA LavenderBlush = new RGBA(255, 240, 245);
	public static final RGBA LawnGreen = new RGBA(124, 252, 0);
	public static final RGBA LemonChiffon = new RGBA(255, 250, 205);
	public static final RGBA LightBlue = new RGBA(173, 216, 230);
	public static final RGBA LightCoral = new RGBA(240, 128, 128);
	public static final RGBA LightCyan = new RGBA(224, 255, 255);
	public static final RGBA LightGoldenrodYellow = new RGBA(250, 250, 210);
	public static final RGBA LightGreen = new RGBA(144, 238, 144);
	public static final RGBA LightGrey = new RGBA(211, 211, 211);
	public static final RGBA LightPink = new RGBA(255, 182, 193);
	public static final RGBA LightSalmon = new RGBA(255, 160, 122);
	public static final RGBA LightSeaGreen = new RGBA(32, 178, 170);
	public static final RGBA LightSkyBlue = new RGBA(135, 206, 250);
	public static final RGBA LightSlateGray = new RGBA(119, 136, 153);
	public static final RGBA LightSteelBlue = new RGBA(176, 196, 222);
	public static final RGBA LightYellow = new RGBA(255, 255, 224);
	public static final RGBA Lime = new RGBA(0, 255, 0);
	public static final RGBA LimeGreen = new RGBA(50, 205, 50);
	public static final RGBA Linen = new RGBA(250, 240, 230);
	public static final RGBA Magenta = new RGBA(255, 0, 255);
	public static final RGBA Maroon = new RGBA(128, 0, 0);
	public static final RGBA MediumAquamarine = new RGBA(102, 205, 170);
	public static final RGBA MediumBlue = new RGBA(0, 0, 205);
	public static final RGBA MediumOrchid = new RGBA(186, 85, 211);
	public static final RGBA MediumPurple = new RGBA(147, 112, 219);
	public static final RGBA MediumSeaGreen = new RGBA(60, 179, 113);
	public static final RGBA MediumSlateBlue = new RGBA(123, 104, 238);
	public static final RGBA MediumSpringGreen = new RGBA(0, 250, 154);
	public static final RGBA MediumTurquoise = new RGBA(72, 209, 204);
	public static final RGBA MediumVioletRed = new RGBA(199, 21, 133);
	public static final RGBA MidnightBlue = new RGBA(25, 25, 112);
	public static final RGBA MintCream = new RGBA(245, 255, 250);
	public static final RGBA MistyRose = new RGBA(255, 228, 225);
	public static final RGBA Moccasin = new RGBA(255, 228, 181);
	public static final RGBA NavajoWhite = new RGBA(255, 222, 173);
	public static final RGBA Navy = new RGBA(0, 0, 128);
	public static final RGBA OldLace = new RGBA(253, 245, 230);
	public static final RGBA Olive = new RGBA(128, 128, 0);
	public static final RGBA OliveDrab = new RGBA(107, 142, 35);
	public static final RGBA Orange = new RGBA(255, 165, 0);
	public static final RGBA OrangeRed = new RGBA(255, 69, 0);
	public static final RGBA Orchid = new RGBA(218, 112, 214);
	public static final RGBA PaleGoldenrod = new RGBA(238, 232, 170);
	public static final RGBA PaleGreen = new RGBA(152, 251, 152);
	public static final RGBA PaleTurquoise = new RGBA(175, 238, 238);
	public static final RGBA PaleVioletRed = new RGBA(219, 112, 147);
	public static final RGBA PapayaWhip = new RGBA(255, 239, 213);
	public static final RGBA PeachPuff = new RGBA(255, 218, 185);
	public static final RGBA Peru = new RGBA(205, 133, 63);
	public static final RGBA Pink = new RGBA(255, 192, 203);
	public static final RGBA Plum = new RGBA(221, 160, 221);
	public static final RGBA PowderBlue = new RGBA(176, 224, 230);
	public static final RGBA Purple = new RGBA(128, 0, 128);
	public static final RGBA Red = new RGBA(255, 0, 0);
	public static final RGBA RosyBrown = new RGBA(188, 143, 143);
	public static final RGBA RoyalBlue = new RGBA(65, 105, 225);
	public static final RGBA SaddleBrown = new RGBA(139, 69, 19);
	public static final RGBA Salmon = new RGBA(250, 128, 114);
	public static final RGBA SandyBrown = new RGBA(244, 164, 96);
	public static final RGBA SeaGreen = new RGBA(46, 139, 87);
	public static final RGBA Seashell = new RGBA(255, 245, 238);
	public static final RGBA Sienna = new RGBA(160, 82, 45);
	public static final RGBA Silver = new RGBA(192, 192, 192);
	public static final RGBA SkyBlue = new RGBA(135, 206, 235);
	public static final RGBA SlateBlue = new RGBA(106, 90, 205);
	public static final RGBA SlateGray = new RGBA(112, 128, 144);
	public static final RGBA Snow = new RGBA(255, 250, 250);
	public static final RGBA SpringGreen = new RGBA(0, 255, 127);
	public static final RGBA SteelBlue = new RGBA(70, 130, 180);
	public static final RGBA Tan = new RGBA(210, 180, 140);
	public static final RGBA Teal = new RGBA(0, 128, 128);
	public static final RGBA Thistle = new RGBA(216, 191, 216);
	public static final RGBA Tomato = new RGBA(255, 99, 71);
	public static final RGBA Turquoise = new RGBA(64, 224, 208);
	public static final RGBA Violet = new RGBA(238, 130, 238);
	public static final RGBA Wheat = new RGBA(245, 222, 179);
	public static final RGBA White = new RGBA(255, 255, 255);
	public static final RGBA WhiteSmoke = new RGBA(245, 245, 245);
	public static final RGBA Yellow = new RGBA(255, 255, 0);
	public static final RGBA YellowGreen = new RGBA(154, 205, 50);

	public RGBA() {
		id = 1;
	}

	public RGBA(int rgba) {
		id = 1;
		this.rgb = rgba;
	}
	
	public RGBA(RGB rgb) {
		this(rgb.r(),rgb.g(),rgb.b(), rgb.isTransparent() ? ((RGBA)rgb).a() : 255);
	}

	public RGBA(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public RGBA(int r, int g, int b, int a) {
		id = 1;
		if (a < 255)
			_isTransparent = true;
		rgb = ((a << 24) | (r << 16) | (g << 8) | b);
	}
	
	public Color asJavaAwtColor() {
		if (isTransparent())
			return new Color(r(),g(),b(),a());
		else
			return new Color(rgb());
	}

	public int a() {
		return (rgb >> 24) & 0xFF;
	}
	
	public static RGBA lerpA(RGB c1, RGBA c2, float amount) {
		return lerpA(new RGBA(c1),c2,amount);
	}
	
	public static RGBA lerpA(RGBA c1, RGB c2, float amount) {
		return lerpA(c1,new RGBA(c2),amount);
	}
	
	public static RGBA lerpA(RGB c1, RGB c2, float amount) {
		return lerpA(new RGBA(c1),new RGBA(c2),amount);
	}

	public static RGBA lerpA(RGBA c1, RGBA c2, float amount) {
		if (c1 == null)
			c1 = White;
		if (c2 == null)
			c2 = White;
		int r = (int) Display.lerp(c1.r(), c2.r(), amount);
		int g = (int) Display.lerp(c1.g(), c2.g(), amount);
		int b = (int) Display.lerp(c1.b(), c2.b(), amount);
		int a = (int) Display.lerp(c1.a(), c2.a(), amount);
		return new RGBA(r, g, b,a);
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

	public static RGBA avg(RGBA... c) {
		int r = 0;
		int g = 0;
		int b = 0;
		int a = 0;
		int l = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == null)
				continue;
			l++;
			r += c[i].r();
			g += c[i].g();
			b += c[i].b();
			a += c[i].a();
		}
		if (l == 0)
			return RGBA.Black;
		return new RGBA((int) (r / l), (int) (g / l), (int) (b / l),
				(int) (a / l));
	}

	public String toString() {
		return "(" + r() + "," + g() + "," + b() + "," + a() + ")";
	}
}
