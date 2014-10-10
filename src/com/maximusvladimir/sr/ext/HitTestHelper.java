package com.maximusvladimir.sr.ext;

import com.maximusvladimir.sr.Point3D;

public class HitTestHelper {
	public static boolean doesRayHitTriangle(Point3D origin, Point3D direction, Point3D v1, Point3D v2, Point3D v3) {
		Point3D edge1 = new Point3D(v2.x - v1.x,v2.y - v1.y, v2.z - v1.z);
		Point3D edge2 = new Point3D(v3.x - v1.x,v3.y - v1.y, v3.z - v1.z);
		
		Point3D p = Point3D.cross(direction, edge2);
		float determinant = Point3D.dot(edge1, p);
		if (determinant > -0.0001f && determinant < 0.0001f)
			return false;
		
		float ideterminant = 1.0f / determinant;
		
		Point3D t = new Point3D(origin.x - v1.x,origin.y - v1.y, origin.z - v1.z);
		float u = Point3D.dot(t, p) * ideterminant;
		if (u < 0 || u > 1)
			return false;
		
		Point3D q = Point3D.cross(t, edge1);
		float v = Point3D.dot(direction, q) * ideterminant;
		if (v < 0 || v+u > 1)
			return false;
		
		float t2 = Point3D.dot(edge2, q) * ideterminant;
		//System.out.println(u+","+v+"reeeee"+t2);
		if (t2 > 0)
			return true;
		return false;
	}
}
