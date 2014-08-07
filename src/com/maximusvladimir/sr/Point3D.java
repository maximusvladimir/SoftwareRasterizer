package com.maximusvladimir.sr;

import com.maximusvladimir.sr.math.Matrix;

public class Point3D extends Operation {
	public float x, y, z;
	public static final Point3D Up = new Point3D(0, 1, 0);
	public static final Point3D Zero = new Point3D();

	public Point3D() {
		this(0, 0, 0);
	}

	public Point3D(Point3D copy) {
		this(copy.x, copy.y, copy.z);
	}

	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		id = 0;
	}

	public float length() {
		return (float) Math.sqrt((x * x) + (y * y) + (z * z));
	}

	public Point3D cross(Point3D p) {
		return cross(this, p);
	}

	public float dot(Point3D p) {
		return dot(this, p);
	}

	public void normalize() {
		float l = length();
		if (l != 0) {
			float inv = 1.0f / l;
			x *= inv;
			y *= inv;
			z *= inv;
		}
	}

	public Point3D convertTo2D(Matrix mvp) {
		Point3D vector = new Point3D();
		Matrix transform = mvp;
		vector.x = (x * transform.M11) + (y * transform.M21)
				+ (z * transform.M31) + transform.M41;
		vector.y = (x * transform.M12) + (y * transform.M22)
				+ (z * transform.M32) + transform.M42;
		vector.z = (x * transform.M13) + (y * transform.M23)
				+ (z * transform.M33) + transform.M43;
		float w = 1f / ((x * transform.M14) + (y * transform.M24)
				+ (z * transform.M34) + transform.M44);
		Point3D point = new Point3D(x * w, y * w, vector.z * w);
		float x = point.x * mvp.Width + mvp.Width / 2.0f;
		float y = -point.y * mvp.Height + mvp.Height / 2.0f;
		return new Point3D(x, y, point.z);
	}

	public static Point3D normalize(Point3D p) {
		Point3D np = new Point3D(p);
		np.normalize();
		return np;
	}

	public static float length(Point3D p) {
		return p.length();
	}

	public static float dot(Point3D p1, Point3D p2) {
		return (p1.x * p2.x) + (p1.y * p2.y) + (p1.z * p2.z);
	}

	public static Point3D cross(Point3D p1, Point3D p2) {
		return new Point3D((p1.y * p2.z) - (p1.z * p2.y), (p1.z * p2.x)
				- (p1.x * p2.z), (p1.x * p2.y) - (p1.y * p2.x));
	}

	public static Point3D add(Point3D p1, Point3D p2) {
		return new Point3D(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
	}

	public static Point3D sub(Point3D p1, Point3D p2) {
		return new Point3D(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
	}
}
