package com.maximusvladimir.sr.math;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import com.maximusvladimir.sr.ActiveTexture;
import com.maximusvladimir.sr.DisplayList;
import com.maximusvladimir.sr.GL;
import com.maximusvladimir.sr.Normal;
import com.maximusvladimir.sr.Operation;
import com.maximusvladimir.sr.Point3D;
import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.RGBA;
import com.maximusvladimir.sr.Texture;
import com.maximusvladimir.sr.TextureCoord;
import com.maximusvladimir.sr.Tuple4;
import com.maximusvladimir.sr.VertexData;
import com.maximusvladimir.sr.flags.DepthMode;
import com.maximusvladimir.sr.flags.TextureBlending;

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
		// Manually setting the pixels, is faster, until you reach about 4x4
		// sizes.
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

	private static VertexData _structSwap = new VertexData();

	public static void drawTriangle(ImageData data, VertexData d1,
			VertexData d2, VertexData d3) {
		// TODO: convert Point3D to Point3I for speed up!!!!!
		// TODO
		// TODO
		// TODO
		// TODO
		// TODO
		// TODO
		// TODO
		// TODO
		if (d1.p.z < 1 || d2.p.z < 1 || d3.p.z < 1)
			return;
		Tuple4 vswap;
		RGB cTmp;
		VertexData swp;
		if (d1.p.y > d2.p.y) {
			swp = d1;
			d1 = d2;
			d2 = swp;
		}
		if (d1.p.y > d3.p.y) {
			swp = d1;
			d1 = d3;
			d3 = swp;
		}
		if (d2.p.y > d3.p.y) {
			swp = d2;
			d2 = d3;
			d3 = swp;
		}
		if (d2.p.y == d3.p.y) {
			lowerTri(data, d1, d2, d3);
		} else if (d1.p.y == d2.p.y) {
			upperTri(data, d1, d2, d3);
		} else {
			float d2sd1 = d2.p.y - d1.p.y;
			float d3sd1 = d3.p.y - d1.p.y;
			float d2sd1Dd3sd1 = d2sd1 / d3sd1;
			vswap = new Tuple4(
					(int) (d1.p.x + d2sd1Dd3sd1 * (d3.p.x - d1.p.x)), d2.p.y,
					d2.p.z,d2.p.w);

			float tcU = d1.t.u() + d2sd1Dd3sd1 * (d3.t.u() - d1.t.u());
			float tcV = d1.t.v() + d2sd1Dd3sd1 * (d3.t.v() - d1.t.v());

			float cBlue = d1.c.b() + d2sd1Dd3sd1 * (d3.c.b() - d1.c.b());
			float cRed = d1.c.r() + d2sd1Dd3sd1 * (d3.c.r() - d1.c.r());
			float cGreen = d1.c.g() + d2sd1Dd3sd1 * (d3.c.g() - d1.c.g());
			float cDepth = d1.p.z + d2sd1Dd3sd1 * (d3.p.z - d1.p.z);
			float cDepthW = d1.p.w + d2sd1Dd3sd1 * (d3.p.w - d1.p.w);
			cTmp = new RGB((int) cRed, (int) cGreen, (int) cBlue);
			vswap.z = cDepth;
			vswap.w = cDepthW;
			_structSwap.c = cTmp;
			_structSwap.p = vswap;
			_structSwap.t = new TextureCoord(tcU, tcV);
			lowerTri(data, d1, d2, _structSwap);
			upperTri(data, d2, _structSwap, d3);
		}
	}

	public static int calculateDepth(ImageData data, float z) {
		// float depth = (float) Math.pow(
		// Math.pow((data.zfar - data.znear), 50), z) * 255 - 255;
		// System.out.println(z);
		// System.out.println(z);
		int is = (int) ((z - 1) * 62000);
		if (is > 255)
			is = 255;
		if (is < 0)
			is = 0;
		return is;
	}

	private static void upperTri(ImageData img, VertexData d1, VertexData d2,
			VertexData d3) {
		float slope1 = (float) (d3.p.x - d1.p.x) / (float) (d3.p.y - d1.p.y);
		float slope2 = (float) (d3.p.x - d2.p.x) / (float) (d3.p.y - d2.p.y);
		float x1 = d3.p.x;
		float x2 = d3.p.x + 0.5f;
		boolean blend = !(img.activeTex != null && img.activeTex
				.getTextureBlending() == TextureBlending.JustTexture);
		boolean isDepth = img.depthMode == DepthMode.PerPixel;
		float p1pDiff = 1.0f / (float) (d3.p.y - d1.p.y);
		float colorSlopeBlue1 = 0;
		float colorSlopeDepth1 = 0;
		float colorSlopeDepthW1 = 0;
		float colorSlopeRed1 = 0;
		float colorSlopeGreen1 = 0;
		float tcSlopeU1 = 0;
		float tcSlopeV1 = 0;
		if (img.gl.isTexturingEnabled()) {
			tcSlopeU1 = (float) (d3.t.u() - d1.t.u()) * p1pDiff;
			tcSlopeV1 = (float) (d3.t.v() - d1.t.v()) * p1pDiff;
		}
		if (isDepth) {
			colorSlopeDepth1 = (float) (d3.p.z - d1.p.z) * p1pDiff;
			colorSlopeDepthW1 = (float) (d3.p.w - d1.p.w) * p1pDiff;
		}
		if (blend) {
			colorSlopeBlue1 = (float) (d3.c.b() - d1.c.b()) * p1pDiff;
			colorSlopeRed1 = (float) (d3.c.r() - d1.c.r()) * p1pDiff;
			colorSlopeGreen1 = (float) (d3.c.g() - d1.c.g()) * p1pDiff;
		}
		float p3pDiff = 1.0f / (float) (d3.p.y - d2.p.y);
		float colorSlopeBlue2 = 0;
		float colorSlopeDepth2 = 0;
		float colorSlopeDepthW2 = 0;
		float colorSlopeRed2 = 0;
		float colorSlopeGreen2 = 0;
		float tcSlopeU2 = 0;
		float tcSlopeV2 = 0;
		if (img.gl.isTexturingEnabled()) {
			tcSlopeU2 = (float) (d3.t.u() - d2.t.u()) * p3pDiff;
			tcSlopeV2 = (float) (d3.t.v() - d2.t.v()) * p3pDiff;
		}
		if (isDepth) {
			colorSlopeDepth2 = (float) (d3.p.z - d2.p.z) * p3pDiff;
			colorSlopeDepthW2 = (float) (d3.p.z - d2.p.z) * p3pDiff;
		}
		if (blend) {
			colorSlopeBlue2 = (float) (d3.c.b() - d2.c.b()) * p3pDiff;
			colorSlopeRed2 = (float) (d3.c.r() - d2.c.r()) * p3pDiff;
			colorSlopeGreen2 = (float) (d3.c.g() - d2.c.g()) * p3pDiff;
		}
		float cBlue1 = 0;
		float cRed1 = 0;
		float cGreen1 = 0;
		float cBlue2 = 0;
		float cRed2 = 0;
		float cGreen2 = 0;
		if (blend) {
			cBlue1 = d3.c.b();
			cRed1 = d3.c.r();
			cGreen1 = d3.c.g();
			cBlue2 = cBlue1;
			cRed2 = cRed1;
			cGreen2 = cGreen1;
		}
		float cDepth1 = d3.p.z;
		float cDepth2 = d3.p.z;
		float cDepthW1 = d3.p.w;
		float cDepthW2 = d3.p.w;
		float tcU1 = d3.t.u();
		float tcU2 = d3.t.u();
		float tcV1 = d3.t.v();
		float tcV2 = d3.t.v();
		if (slope1 < slope2) {
			float slopeTmp = slope1;
			slope1 = slope2;
			slope2 = slopeTmp;

			if (blend) {
				slopeTmp = colorSlopeRed1;
				colorSlopeRed1 = colorSlopeRed2;
				colorSlopeRed2 = slopeTmp;

				slopeTmp = colorSlopeGreen1;
				colorSlopeGreen1 = colorSlopeGreen2;
				colorSlopeGreen2 = slopeTmp;

				slopeTmp = colorSlopeBlue1;
				colorSlopeBlue1 = colorSlopeBlue2;
				colorSlopeBlue2 = slopeTmp;
			}

			if (isDepth) {
				slopeTmp = colorSlopeDepth1;
				colorSlopeDepth1 = colorSlopeDepth2;
				colorSlopeDepth2 = slopeTmp;
				
				slopeTmp = colorSlopeDepthW1;
				colorSlopeDepthW1 = colorSlopeDepthW2;
				colorSlopeDepthW2 = slopeTmp;
			}
			if (img.gl.isTexturingEnabled()) {
				slopeTmp = tcSlopeU1;
				tcSlopeU1 = tcSlopeU2;
				tcSlopeU2 = slopeTmp;

				slopeTmp = tcSlopeV1;
				tcSlopeV1 = tcSlopeV2;
				tcSlopeV2 = slopeTmp;
			}
		}
		for (int y = (int) d3.p.y; y > d1.p.y; y--) {
			x1 -= slope1;
			x2 -= slope2;
			if (blend) {
				cRed1 -= colorSlopeRed1;
				cGreen1 -= colorSlopeGreen1;
				cBlue1 -= colorSlopeBlue1;
				cRed2 -= colorSlopeRed2;
				cGreen2 -= colorSlopeGreen2;
				cBlue2 -= colorSlopeBlue2;
			}
			if (isDepth) {
				cDepth2 -= colorSlopeDepth2;
				cDepth1 -= colorSlopeDepth1;
				cDepthW2 -= colorSlopeDepthW2;
				cDepthW1 -= colorSlopeDepthW1;
			}
			if (img.gl.isTexturingEnabled()) {
				tcU1 -= tcSlopeU1;
				tcV1 -= tcSlopeV1;
				tcU2 -= tcSlopeU2;
				tcV2 -= tcSlopeV2;
			}
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
			boolean istransvalid = img.activeTex != null
					&& img.activeTex.getTransparentColor() != null;
			RGB col = new RGB();
			while (xs < xe) {
				float t = (xs - x1) / den;
				float it = 1 - t;
				int rc = 0;
				int gc = 0;
				int bc = 0;
				if (blend) {
					rc = (int) (it * cRed1 + t * cRed2);
					gc = (int) (it * cGreen1 + t * cGreen2);
					bc = (int) (it * cBlue1 + t * cBlue2);
				}
				if (img.gl.isTexturingEnabled() && img.activeTex != null) {
					float tu = (it * tcU1 + t * tcU2);
					float tv = (it * tcV1 + t * tcV2);
					col = img.activeTex.lookup(tu, tv);
					if (istransvalid
							&& col.rgb() == img.activeTex.getTransparentColor()
									.rgb()) {
						xs++;
						continue;
					}
					if (blend)
						col = RGB.lerp(col, new RGB(rc, gc, bc), 0.5f);
				} else
					col.set(rc, gc, bc);
				if (isDepth) {
					int depth = calculateDepth(img,
							(it * cDepth1 + t * cDepth2));
					float act = (it * cDepthW1 + t * cDepthW2);
					if (act <= 0) {
						xs++;
						continue;
					}
					if (img.gl.getFogEnabled()
							&& img.gl.getFogEquation() != null)
						col = RGB.lerp(col, img.gl.getFogColor(), img.gl
								.getFogEquation().calculateFog(act));
					img.setPixel(xs, y, depth, col);
				} else
					img.setPixel(xs, y, 0, col);
				// setPixel(xs, y, rc, gc, bc, bck);
				xs++;
			}

		}
	}

	private static void lowerTri(ImageData img, VertexData d1, VertexData d2,
			VertexData d3) {
		float slope1 = (float) (d2.p.x - d1.p.x) / (float) (d2.p.y - d1.p.y);
		float slope2 = (float) (d3.p.x - d1.p.x) / (float) (d3.p.y - d1.p.y);
		float x1 = d1.p.x;
		float x2 = d1.p.x + 0.5f;
		boolean blend = !(img.activeTex != null && img.activeTex
				.getTextureBlending() == TextureBlending.JustTexture);
		boolean isDepth = img.depthMode == DepthMode.PerPixel;
		float va2va1Diff = 1.0f / (float) (d2.p.y - d1.p.y);
		float colorSlopeBlue1 = 0;
		float colorSlopeRed1 = 0;
		float colorSlopeGreen1 = 0;
		float tcSlopeU1 = 0;
		float tcSlopeV1 = 0;
		boolean hasImage = img.activeTex != null;
		if (img.gl.isTexturingEnabled() && hasImage) {
			tcSlopeU1 = (float) (d2.t.u() - d1.t.u()) * va2va1Diff;
			tcSlopeV1 = (float) (d2.t.v() - d1.t.v()) * va2va1Diff;
		}
		if (blend) {
			colorSlopeBlue1 = (float) (d2.c.b() - d1.c.b()) * va2va1Diff;
			colorSlopeRed1 = (float) (d2.c.r() - d1.c.r()) * va2va1Diff;
			colorSlopeGreen1 = (float) (d2.c.g() - d1.c.g()) * va2va1Diff;
		}
		float va3va1Diff = 1.0f / (float) (d3.p.y - d1.p.y);
		float colorSlopeBlue2 = 0;
		float colorSlopeRed2 = 0;
		float colorSlopeGreen2 = 0;
		float colorSlopeDepth1 = 0;
		float colorSlopeDepth2 = 0;
		float colorSlopeDepthW1 = 0;
		float colorSlopeDepthW2 = 0;
		float tcSlopeU2 = 0;
		float tcSlopeV2 = 0;
		if (img.gl.isTexturingEnabled() && hasImage) {
			tcSlopeU2 = (float) (d3.t.u() - d1.t.u()) * va3va1Diff;
			tcSlopeV2 = (float) (d3.t.v() - d1.t.v()) * va3va1Diff;
		}
		if (isDepth) {
			colorSlopeDepth1 = (float) (d2.p.z - d1.p.z) * va2va1Diff;
			colorSlopeDepth2 = (float) (d3.p.z - d1.p.z) * va3va1Diff;
			colorSlopeDepthW1 = (float) (d2.p.w - d1.p.w) * va2va1Diff;
			colorSlopeDepthW2 = (float) (d3.p.w - d1.p.w) * va3va1Diff;
		}
		if (blend) {
			colorSlopeBlue2 = (float) (d3.c.b() - d1.c.b()) * va3va1Diff;
			colorSlopeRed2 = (float) (d3.c.r() - d1.c.r()) * va3va1Diff;
			colorSlopeGreen2 = (float) (d3.c.g() - d1.c.g()) * va3va1Diff;
		}
		float cBlue1 = 0;
		float cRed1 = 0;
		float cGreen1 = 0;
		float cBlue2 = 0;
		float cRed2 = 0;
		float cGreen2 = 0;
		if (blend) {
			cBlue1 = d1.c.b();
			cRed1 = d1.c.r();
			cGreen1 = d1.c.g();
			cBlue2 = cBlue1;
			cRed2 = cRed1;
			cGreen2 = cGreen1;
		}
		float cDepth1 = d1.p.z;
		float cDepth2 = d1.p.z;
		float cDepthW1 = d1.p.w;
		float cDepthW2 = d1.p.w;
		float tcU1 = d1.t.u();
		float tcU2 = d1.t.u();
		float tcV1 = d1.t.v();
		float tcV2 = d1.t.v();
		if (slope2 < slope1) {
			float slopeTmp = slope1;
			slope1 = slope2;
			slope2 = slopeTmp;

			if (blend) {
				slopeTmp = colorSlopeRed1;
				colorSlopeRed1 = colorSlopeRed2;
				colorSlopeRed2 = slopeTmp;

				slopeTmp = colorSlopeGreen1;
				colorSlopeGreen1 = colorSlopeGreen2;
				colorSlopeGreen2 = slopeTmp;

				slopeTmp = colorSlopeBlue1;
				colorSlopeBlue1 = colorSlopeBlue2;
				colorSlopeBlue2 = slopeTmp;
			}

			if (isDepth) {
				slopeTmp = colorSlopeDepth1;
				colorSlopeDepth1 = colorSlopeDepth2;
				colorSlopeDepth2 = slopeTmp;
				
				slopeTmp = colorSlopeDepthW1;
				colorSlopeDepthW1 = colorSlopeDepthW2;
				colorSlopeDepthW2 = slopeTmp;
			}

			if (img.gl.isTexturingEnabled() && hasImage) {
				slopeTmp = tcSlopeU1;
				tcSlopeU1 = tcSlopeU2;
				tcSlopeU2 = slopeTmp;

				slopeTmp = tcSlopeV1;
				tcSlopeV1 = tcSlopeV2;
				tcSlopeV2 = slopeTmp;
			}
		}
		RGB col = new RGB();
		boolean istransvalid = img.activeTex != null
				&& img.activeTex.getTransparentColor() != null;
		for (int y = (int) d1.p.y; y <= d2.p.y; y++) {
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
				float it = 1 - t;
				int rc = 0;
				int gc = 0;
				int bc = 0;
				if (blend) {
					rc = (int) (it * cRed1 + t * cRed2);
					gc = (int) (it * cGreen1 + t * cGreen2);
					bc = (int) (it * cBlue1 + t * cBlue2);
				}
				// boolean trans = false;
				if (img.gl.isTexturingEnabled() && hasImage) {
					float tu = (it * tcU1 + t * tcU2);
					float tv = (it * tcV1 + t * tcV2);
					// System.out.println(tu + "," + tv);
					col = img.activeTex.lookup(tu, tv);
					if (istransvalid
							&& col.rgb() == img.activeTex.getTransparentColor()
									.rgb()) {
						xs++;
						continue;
					}
					if (blend) {
						col = RGB.lerp(col, new RGB(rc, gc, bc), 0.5f);
					}
					// if (col.rgb() == -1)
					// trans = true;
				} else {
					col.set(rc, gc, bc);
				}
				// if (!trans) {
				if (img.depthMode == DepthMode.PerPixel) {
					int depth = calculateDepth(img,
							(it * cDepth1 + t * cDepth2));
					float act = (it * cDepthW1 + t * cDepthW2);
					if (act <= 0) {
						xs++;
						continue;
					}
					if (img.gl.getFogEnabled()
							&& img.gl.getFogEquation() != null)
						col = RGB.lerp(col, img.gl.getFogColor(), img.gl
								.getFogEquation().calculateFog(act));
					img.setPixel(xs,y, depth, col);
				} else
					img.setPixel(xs, y, 0, col);
				// }
				xs++;
			}
			x1 += slope1;
			x2 += slope2;
			if (blend) {
			cRed1 += colorSlopeRed1;
			cGreen1 += colorSlopeGreen1;
			cBlue1 += colorSlopeBlue1;
			cRed2 += colorSlopeRed2;
			cGreen2 += colorSlopeGreen2;
			cBlue2 += colorSlopeBlue2;
			}
			if (isDepth) {
				cDepth1 += colorSlopeDepth1;
				cDepth2 += colorSlopeDepth2;
				cDepthW1 += colorSlopeDepthW1;
				cDepthW2 += colorSlopeDepthW2;
			}
			if (img.gl.isTexturingEnabled()) {
				tcU1 += tcSlopeU1;
				tcV1 += tcSlopeV1;
				tcU2 += tcSlopeU2;
				tcV2 += tcSlopeV2;
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
					data.setPixel(xx, yy, calculateDepth(data, depth),
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

	public static final void convertOperationsToTriangles(GL gl,
			ArrayList<Operation> _operations, ArrayList<Triangle> _triangles) {
		_triangles.clear();
		Triangle t = new Triangle();
		Matrix mvp = new Matrix();
		for (int i = 0; i < _operations.size(); i++) {
			Operation op = _operations.get(i);
			if (op.id == 0) {
				// Vertex
				Point3D point = (Point3D) op;
				// point.negate();
				// point.z *= -1;
				if (t.p1 == null)
					t.p1 = point;
				else if (t.p2 == null)
					t.p2 = point;
				else {
					t.p3 = point;
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
				// None

			} else if (op.id == 5) {
				mvp = (Matrix) op;
			} else if (op.id == 6) {
				_triangles.addAll(((DisplayList) op).getData());
			} else if (op.id == 7) {
				int id = ((ActiveTexture) op).getTextureId();
				if (id >= 0)
					t.texture = gl.getTexture(id);
				else
					t.texture = null;
			}
		}
		Collections.reverse(_triangles);
	}
}
