package com.maximusvladimir.sr;

public class Point3I{
	public int x, y, z;
	public static final Point3I Up = new Point3I(0, 1, 0);
	public static final Point3I Zero = new Point3I();

	public Point3I() {
		this(0, 0, 0);
	}

	public Point3I(Point3I copy) {
		this(copy.x, copy.y, copy.z);
	}
	
	public Point3I(Point3D copy) {
		this(copy.x,copy.y,copy.z);
	}

	public Point3I(float x, float y, float z) {
		this.x = (int)x;
		this.y = (int)y;
		this.z = (int)z;
	}
	
	public Point3I(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}

	public float length() {
		return (float) Math.sqrt(((float)x * x) + ((float)y * y) + ((float)z * z));
	}

	public Point3I cross(Point3I p) {
		return cross(this, p);
	}

	public float dot(Point3I p) {
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

	/*public Point3I convertTo2D(Matrix mvp,float wi, float he) {
		Point3I vector = new Point3I();
		Matrix transform = mvp;
		vector.x = (x * transform.M11) + (y * transform.M21)
				+ (z * transform.M31) + transform.M41;
		vector.y = (x * transform.M12) + (y * transform.M22)
				+ (z * transform.M32) + transform.M42;
		vector.z = (x * transform.M13) + (y * transform.M23)
				+ (z * transform.M33) + transform.M43;
		float w = 1f / ((x * transform.M14) + (y * transform.M24)
				+ (z * transform.M34) + transform.M44);
		Point3I point = new Point3I(x * w, y * w, vector.z * w);
		float x = point.x * wi + wi / 2.0f;
		float y = -point.y * he + he / 2.0f;
		return new Point3I(x, y, point.z);
	}*/

	public static Point3I normalize(Point3I p) {
		Point3I np = new Point3I(p);
		np.normalize();
		return np;
	}

	public static float length(Point3I p) {
		return p.length();
	}

	public static float dot(Point3I p1, Point3I p2) {
		return (p1.x * p2.x) + (p1.y * p2.y) + (p1.z * p2.z);
	}

	public static Point3I cross(Point3I p1, Point3I p2) {
		return new Point3I((p1.y * p2.z) - (p1.z * p2.y), (p1.z * p2.x)
				- (p1.x * p2.z), (p1.x * p2.y) - (p1.y * p2.x));
	}

	public static Point3I add(Point3I p1, Point3I p2) {
		return new Point3I(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
	}

	public static Point3I sub(Point3I p1, Point3I p2) {
		return new Point3I(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
	}
}
