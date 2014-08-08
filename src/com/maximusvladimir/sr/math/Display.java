package com.maximusvladimir.sr.math;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import com.maximusvladimir.sr.DisplayList;
import com.maximusvladimir.sr.Normal;
import com.maximusvladimir.sr.Operation;
import com.maximusvladimir.sr.Point3D;
import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.RGBA;
import com.maximusvladimir.sr.Texture;
import com.maximusvladimir.sr.TextureCoord;
import com.maximusvladimir.sr.flags.DepthMode;

public class Display {
	public static final float lerp(float a, float b, float c) {
		return a + c * (b - a);
	}

	public static final void drawPoint(ImageData data, int size, Point3D p,
			RGB c) {
		if (size == 0)
			return;
		if (c == null)
			c = data.lastGoodColor;
		int x = (int) p.x;
		int y = (int) p.y;
		// Manually setting the pixels, is faster, until you reach about 4x4 sizes.
		if (size == 1)
			data.setPixel(x, y, c);
		else if (size == 2) {
			data.setPixel(x, y, c);
			data.setPixel(x + 1, y, c);
			data.setPixel(x, y + 1, c);
			data.setPixel(x + 1, y + 1, c);
		} else if (size == 3) {
			data.setPixel(x, y, c);
			data.setPixel(x - 1, y - 1, c);
			data.setPixel(x, y - 1, c);
			data.setPixel(x - 1, y, c);
			data.setPixel(x + 1, y - 1, c);
			data.setPixel(x - 1, y + 1, c);
			data.setPixel(x + 1, y + 1, c);
			data.setPixel(x + 1, y, c);
			data.setPixel(x, y + 1, c);
		} else {
			if (c.isTransparent())
				data.g.setColor(new Color(c.rgb(), true));
			else
				data.g.setColor(new Color(c.rgb()));
			int s2 = size / 2;
			data.g.fillRect(x - s2, y - s2, size, size);
		}
	}

	public static final void drawLine(ImageData img, Point3D p1, Point3D p2,
			RGB c1, RGB c2) {
		if (c1 != null) {
			if (c1.isTransparent())
				img.g.setColor(new Color(c1.rgb(), true));
			else
				img.g.setColor(new Color(c1.rgb()));
			img.lastGoodColor = c1;
		} else if (c2 != null) {
			if (c2.isTransparent())
				img.g.setColor(new Color(c2.rgb(), true));
			else
				img.g.setColor(new Color(c2.rgb()));
			img.lastGoodColor = c2;
		} else
			img.g.setColor(img.lastGoodColor.asJavaAwtColor());
		img.g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
	}

	public static float min(float q0, float q1, float q2) {
		if (q0 == q1)
			q0 = q0 - 1;
		if (q0 == q2)
			q2 = q2 - 1;
		if (q0 < q1 && q0 < q2)
			return q0;
		else if (q1 < q0 && q1 < q2)
			return q1;
		else if (q2 < q0 && q2 < q1)
			return q2;
		return 0;
	}

	public static float max(float q0, float q1, float q2) {
		if (q0 == q1)
			q1 = q1 + 1;
		if (q2 == q1)
			q2 = q2 + 1;
		if (q0 > q1 && q0 > q2)
			return q0;
		else if (q1 > q0 && q1 > q2)
			return q1;
		else if (q2 > q0 && q2 > q1)
			return q2;
		return 0;
	}

	public static void DrawTriangle(ImageData data, Point3D p1, Point3D p2,
			Point3D p3, RGB c1, RGB c2, RGB c3) {
		// TODO: convert Point3D to Point3I for speed up!!!!!
		// TODO
		// TODO
		// TODO
		// TODO
		// TODO
		// TODO
		// TODO
		// TODO
		Point3D vswap;
		RGB cTmp;
		if (p1.y > p2.y) {
			cTmp = c1;
			vswap = p1;
			p1 = p2;
			c1 = c2;
			p2 = vswap;
			c2 = cTmp;
		}
		if (p1.y > p3.y) {
			cTmp = c1;
			vswap = p1;
			c1 = c3;
			p1 = p3;
			p3 = vswap;
			c3 = cTmp;
		}
		if (p2.y > p3.y) {
			cTmp = c2;
			vswap = p2;
			p2 = p3;
			c2 = c3;
			p3 = vswap;
			c3 = cTmp;
		}
		if (p2.y == p3.y) {
			lowerTri(data, p1, p2, p3, c1, c2, c3);
		} else if (p1.y == p2.y) {
			upperTri(data, p1, p2, p3, c1, c2, c3);
		} else {
			vswap = new Point3D(
					(p1.x + ((float) (p2.y - p1.y) / (float) (p3.y - p1.y))
							* (p3.x - p1.x)), p2.y, p2.z);
			float cBlue = c1.b()
					+ ((float) (p2.y - p1.y) / (float) (p3.y - p1.y))
					* (c3.b() - c1.b());
			float cRed = c1.r()
					+ ((float) (p2.y - p1.y) / (float) (p3.y - p1.y))
					* (c3.r() - c1.r());
			float cGreen = c1.g()
					+ ((float) (p2.y - p1.y) / (float) (p3.y - p1.y))
					* (c2.g() - c1.g());
			cTmp = new RGB((int) cRed, (int) cGreen, (int) cBlue);
			lowerTri(data, p1, p2, vswap, c1, c2, cTmp);
			upperTri(data, p2, vswap, p3, c2, cTmp, c3);
		}
	}
	
	public static int calculateDepth(ImageData data, float z) {
		//float depth = (float) Math.pow(
			//	Math.pow((data.zfar - data.znear), 50), z) * 255 - 255;
		//System.out.println(z);
		//System.out.println(z);
		int is = (int)((z-1) * 62000);
		if (is > 255)
			is = 255;
		if (is < 0)
			is = 0;
		return is;
	}

	private static void upperTri(ImageData img, Point3D v1, Point3D v2,
			Point3D v3, RGB c1, RGB c2, RGB c3) {
		float slope1 = (float) (v3.x - v1.x) / (float) (v3.y - v1.y);
		float slope2 = (float) (v3.x - v2.x) / (float) (v3.y - v2.y);
		float x1 = v3.x;
		float x2 = v3.x + 0.5f;
		float v3v1Diff = 1.0f / (float) (v3.y - v1.y);
		float colorSlopeBlue1 = (float) (c3.b() - c1.b()) * v3v1Diff;
		float colorSlopeDepth1 = (float) (v3.z - v1.z) * v3v1Diff;
		float colorSlopeRed1 = (float) (c3.r() - c1.r()) / v3v1Diff;
		float colorSlopeGreen1 = (float) (c3.g() - c1.g()) * v3v1Diff;
		float v3v2Diff = 1.0f / (float) (v3.y - v2.y);
		float colorSlopeBlue2 = (float) (c3.b() - c2.b()) * v3v2Diff;
		float colorSlopeDepth2 = (float) (v3.z - v2.z) * v3v2Diff;
		float colorSlopeRed2 = (float) (c3.r() - c2.r()) / v3v2Diff;
		float colorSlopeGreen2 = (float) (c3.g() - c2.g()) * v3v2Diff;
		float cBlue1 = c3.b();
		float cRed1 = c3.r();
		float cGreen1 = c3.g();
		float cBlue2 = c3.b();
		float cRed2 = c3.r();
		float cGreen2 = c3.g();
		float cDepth1 = v3.z;
		float cDepth2 = v3.z;
		if (slope1 < slope2) {
			float slopeTmp = slope1;
			slope1 = slope2;
			slope2 = slopeTmp;

			slopeTmp = colorSlopeRed1;
			colorSlopeRed1 = colorSlopeRed2;
			colorSlopeRed2 = slopeTmp;

			slopeTmp = colorSlopeGreen1;
			colorSlopeGreen1 = colorSlopeGreen2;
			colorSlopeGreen2 = slopeTmp;

			slopeTmp = colorSlopeBlue1;
			colorSlopeBlue1 = colorSlopeBlue2;
			colorSlopeBlue2 = slopeTmp;

			if (img.depthMode == DepthMode.PerPixel) {
				slopeTmp = colorSlopeDepth1;
				colorSlopeDepth1 = colorSlopeDepth2;
				colorSlopeDepth2 = slopeTmp;
			}
		}
		for (int y = (int) v3.y; y > v1.y; y--) {
			x1 -= slope1;
			x2 -= slope2;
			cRed1 -= colorSlopeRed1;
			cGreen1 -= colorSlopeGreen1;
			cBlue1 -= colorSlopeBlue1;
			if (img.depthMode == DepthMode.PerPixel)
				cDepth1 -= colorSlopeDepth1;
			cRed2 -= colorSlopeRed2;
			cGreen2 -= colorSlopeGreen2;
			cBlue2 -= colorSlopeBlue2;
			if (img.depthMode == DepthMode.PerPixel)
				cDepth2 -= colorSlopeDepth2;
			if (y > img.h || y < 0)
				continue;
			int xs = (int) Math.ceil(x1);
			int xe = (int) x2;
			if (xs > img.w || xe > img.w)
				continue;
			if (xe < xs) {
				int swaper = xs;
				xs = xe;
				xe = swaper;
			}
			if (xs < 0)
				xs = 0;
			float den = x2 - x1;
			while (xs < xe) {
				float t = (xs - x1) / den;
				int rc = (int) ((1 - t) * cRed1 + t * cRed2);
				int gc = (int) ((1 - t) * cGreen1 + t * cGreen2);
				int bc = (int) ((1 - t) * cBlue1 + t * cBlue2);
				if (img.depthMode == DepthMode.PerPixel) {
					img.setPixel(xs, y, calculateDepth(img,((1 - t) * cDepth1 + t * cDepth2)), new RGB(rc, gc, bc));
				}
				img.setPixel(xs, y, 0, new RGB(rc, gc, bc));
				// setPixel(xs, y, rc, gc, bc, bck);
			}
			xs++;
		}
	}

	private static void lowerTri(ImageData img, Point3D v1, Point3D v2,
			Point3D v3, RGB c1, RGB c2, RGB c3) {
		float slope1 = (float) (v2.x - v1.x) / (float) (v2.y - v1.y);
		float slope2 = (float) (v3.x - v1.x) / (float) (v3.y - v1.y);
		float x1 = v1.x;
		float x2 = v1.x + 0.5f;
		float v2v1Diff = 1.0f / (float) (v2.y - v1.y);
		float colorSlopeBlue1 = (float) (c2.b() - c1.b()) * v2v1Diff;
		float colorSlopeRed1 = (float) (c2.r() - c1.r()) * v2v1Diff;
		float colorSlopeGreen1 = (float) (c2.g() - c1.g()) * v2v1Diff;
		float v3v1Diff = 1.0f / (float) (v3.y - v1.y);
		float colorSlopeBlue2 = (float) (c3.b() - c1.b()) * v3v1Diff;
		float colorSlopeRed2 = (float) (c3.r() - c1.r()) * v3v1Diff;
		float colorSlopeGreen2 = (float) (c3.g() - c1.g()) * v3v1Diff;
		float colorSlopeDepth1 = (float) (v2.z - v1.z) * v2v1Diff;
		float colorSlopeDepth2 = (float) (v3.z - v1.z) * v3v1Diff;
		float cBlue1 = c1.b();
		float cRed1 = c1.r();
		float cGreen1 = c1.g();
		float cBlue2 = c1.b();
		float cRed2 = c1.r();
		float cGreen2 = c1.g();
		float cDepth1 = v1.z;
		float cDepth2 = v1.z;
		if (slope2 < slope1) {
			float slopeTmp = slope1;
			slope1 = slope2;
			slope2 = slopeTmp;

			slopeTmp = colorSlopeRed1;
			colorSlopeRed1 = colorSlopeRed2;
			colorSlopeRed2 = slopeTmp;

			slopeTmp = colorSlopeGreen1;
			colorSlopeGreen1 = colorSlopeGreen2;
			colorSlopeGreen2 = slopeTmp;

			slopeTmp = colorSlopeBlue1;
			colorSlopeBlue1 = colorSlopeBlue2;
			colorSlopeBlue2 = slopeTmp;
			if (img.depthMode == DepthMode.PerPixel) {
				slopeTmp = colorSlopeDepth1;
				colorSlopeDepth1 = colorSlopeDepth2;
				colorSlopeDepth2 = slopeTmp;
			}
		}
		for (int y = (int) v1.y; y <= v2.y; y++) {
			if (y > img.h || y < 0)
				continue;
			int xs = (int) Math.ceil(x1);
			int xe = (int) x2;
			if (xe > img.w || xs > img.w)
				continue;
			if (xe < xs) {
				int swaper = xs;
				xs = xe;
				xe = swaper;
			}
			if (xs < 0)
				xs = 0;
			float den = x2 - x1;
			while (xs < xe) {
				float t = (xs - x1) / den;
				int rc = (int) ((1 - t) * cRed1 + t * cRed2);
				int gc = (int) ((1 - t) * cGreen1 + t * cGreen2);
				int bc = (int) ((1 - t) * cBlue1 + t * cBlue2);
				if (img.depthMode == DepthMode.PerPixel) {
					img.setPixel(xs, y, calculateDepth(img,((1 - t) * cDepth1 + t * cDepth2)), new RGB(rc, gc, bc));
				} else
					img.setPixel(xs, y, 0, new RGB(rc, gc, bc));
				xs++;
			}
			x1 += slope1;
			x2 += slope2;
			cRed1 += colorSlopeRed1;
			cGreen1 += colorSlopeGreen1;
			cBlue1 += colorSlopeBlue1;
			cRed2 += colorSlopeRed2;
			cGreen2 += colorSlopeGreen2;
			cBlue2 += colorSlopeBlue2;
			if (img.depthMode == DepthMode.PerPixel) {
				cDepth1 += colorSlopeDepth1;
				cDepth2 += colorSlopeDepth2;
			}
		}
	}

	public static final void drawLerpLine(ImageData data, Point3D p1,
			Point3D p2, RGB c1, RGB c2) {
		int x0 = (int) p1.x;
		int y0 = (int) p1.y;
		int x1 = (int) p2.x;
		int y1 = (int) p2.y;
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int sx = 0;
		int sy = 0;
		float d = (float) Math.sqrt(Math.pow(p1.x - p2.x, 2)
				+ Math.pow(p1.y - p2.y, 2));
		if (x0 < x1)
			sx = 1;
		else
			sx = -1;
		if (y0 < y1)
			sy = 1;
		else
			sy = -1;
		float err = dx - dy;
		int c = 0;
		while (true) {
			c++;
			int xx = (int) x0;
			int yy = (int) y0;
			if (data != null) {
				float depth = -Display.lerp(-p1.z, -p2.z, c / d);
				if (c1.isTransparent() || c2.isTransparent())
					data.setPixel(xx, yy, calculateDepth(data,depth),
							RGBA.lerpA(c1, c2, c / d));
				else
					data.setPixel(xx, yy, (int) depth, RGB.lerp(c1, c2, c / d));
			}
			// if (x0 >= x1 - 0.7f && x0 <= x1 + 0.7f && y0 >= y1 - 0.7f
			// && y0 <= y1 + 0.7f)
			if (x0 == x1 && y0 == y1)
				break;
			float e2 = 2 * err;
			if (e2 > -dy) {
				err = err - dy;
				x0 = x0 + sx;
			}
			if (e2 < dx) {
				err = err + dx;
				y0 = y0 + sy;
			}
		}
	}

	public static final void convertOperationsToTriangles(
			ArrayList<Operation> _operations, ArrayList<Triangle> _triangles) {
		_triangles.clear();
		Triangle t = new Triangle();
		Matrix mvp = new Matrix();
		for (int i = 0; i < _operations.size(); i++) {
			Operation op = _operations.get(i);
			if (op.id == 0) {
				// Vertex
				if (t.p1 == null)
					t.p1 = (Point3D) op;
				else if (t.p2 == null)
					t.p2 = (Point3D) op;
				else {
					t.p3 = (Point3D) op;
					t.mvp = mvp;
					_triangles.add(t);
					t = new Triangle();
				}
			} else if (op.id == 1) {
				// Color
				if (t.c1 == null)
					t.c1 = (RGB) op;
				else if (t.c2 == null)
					t.c2 = (RGB) op;
				else
					t.c3 = (RGB) op;
			} else if (op.id == 2) {
				// Texture Coord
				if (t.tc1 == null)
					t.tc1 = (TextureCoord) op;
				else if (t.tc2 == null)
					t.tc2 = (TextureCoord) op;
				else
					t.tc3 = (TextureCoord) op;
			} else if (op.id == 3) {
				// Normal
				if (t.n1 == null)
					t.n1 = (Normal) op;
				else if (t.n2 == null)
					t.n2 = (Normal) op;
				else
					t.n3 = (Normal) op;
			} else if (op.id == 4) {
				t.t = (Texture) op;
			} else if (op.id == 5) {
				mvp = (Matrix) op;
			} else if (op.id == 6) {
				_triangles.addAll(((DisplayList) op).getData());
			}
		}
		Collections.reverse(_triangles);
	}
}
