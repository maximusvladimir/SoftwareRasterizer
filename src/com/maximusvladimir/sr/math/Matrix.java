package com.maximusvladimir.sr.math;

import com.maximusvladimir.sr.Point3D;

public class Matrix {
	public float M11 = 0;
	public float M12 = 0;
	public float M13 = 0;
	public float M14 = 0;
	public float M21 = 0;
	public float M22 = 0;
	public float M23 = 0;
	public float M24 = 0;
	public float M31 = 0;
	public float M32 = 0;
	public float M33 = 0;
	public float M34 = 0;
	public float M41 = 0;
	public float M42 = 0;
	public float M43 = 0;
	public float M44 = 0;
	
	public float Width = -1;
	public float Height = -1;

	public Matrix() {

	}

	public static Matrix createIdentity() {
		Matrix m = new Matrix();
		m.M11 = 1.0f;
		m.M22 = 1.0f;
		m.M33 = 1.0f;
		m.M44 = 1.0f;
		return m;
	}
	
	public static Matrix mul(Matrix m1, Matrix m2) {
		Matrix result = new Matrix();
		if (m1.Width == -1 && m1.Height == -1) {
			result.Width = m2.Width;
			result.Height = m2.Height;
		} else {
			result.Width = m1.Width;
			result.Height = m1.Height;
		}
		Matrix left = m1;
		Matrix right = m2;
		Matrix temp = result;
        temp.M11 = (left.M11 * right.M11) + (left.M12 * right.M21) + (left.M13 * right.M31) + (left.M14 * right.M41);
        temp.M12 = (left.M11 * right.M12) + (left.M12 * right.M22) + (left.M13 * right.M32) + (left.M14 * right.M42);
        temp.M13 = (left.M11 * right.M13) + (left.M12 * right.M23) + (left.M13 * right.M33) + (left.M14 * right.M43);
        temp.M14 = (left.M11 * right.M14) + (left.M12 * right.M24) + (left.M13 * right.M34) + (left.M14 * right.M44);
        temp.M21 = (left.M21 * right.M11) + (left.M22 * right.M21) + (left.M23 * right.M31) + (left.M24 * right.M41);
        temp.M22 = (left.M21 * right.M12) + (left.M22 * right.M22) + (left.M23 * right.M32) + (left.M24 * right.M42);
        temp.M23 = (left.M21 * right.M13) + (left.M22 * right.M23) + (left.M23 * right.M33) + (left.M24 * right.M43);
        temp.M24 = (left.M21 * right.M14) + (left.M22 * right.M24) + (left.M23 * right.M34) + (left.M24 * right.M44);
        temp.M31 = (left.M31 * right.M11) + (left.M32 * right.M21) + (left.M33 * right.M31) + (left.M34 * right.M41);
        temp.M32 = (left.M31 * right.M12) + (left.M32 * right.M22) + (left.M33 * right.M32) + (left.M34 * right.M42);
        temp.M33 = (left.M31 * right.M13) + (left.M32 * right.M23) + (left.M33 * right.M33) + (left.M34 * right.M43);
        temp.M34 = (left.M31 * right.M14) + (left.M32 * right.M24) + (left.M33 * right.M34) + (left.M34 * right.M44);
        temp.M41 = (left.M41 * right.M11) + (left.M42 * right.M21) + (left.M43 * right.M31) + (left.M44 * right.M41);
        temp.M42 = (left.M41 * right.M12) + (left.M42 * right.M22) + (left.M43 * right.M32) + (left.M44 * right.M42);
        temp.M43 = (left.M41 * right.M13) + (left.M42 * right.M23) + (left.M43 * right.M33) + (left.M44 * right.M43);
        temp.M44 = (left.M41 * right.M14) + (left.M42 * right.M24) + (left.M43 * right.M34) + (left.M44 * right.M44);
        return result;
	}
	
	public void setToPerspective(float width, float height, float znear, float zfar) {
		Width = width;
		Height = height;
		float halfWidth = width * 0.5f;
        float halfHeight = height * 0.5f;
		float zRange = zfar / (zfar - znear);
        M11 = 2.0f * znear / (halfWidth + halfWidth);
        M22 = 2.0f * znear / (halfHeight + halfHeight);
        M31 = -(-halfWidth + halfWidth) / (-halfWidth - halfWidth);
        M32 = -(-halfHeight + halfHeight) / (-halfHeight - halfHeight);
        M33 = -zRange;
        M34 = -1.0f;
        M43 = -znear * zRange;
	}

	public void setToLookAt(Point3D position, Point3D lookAt, Point3D up) {
		Point3D z = Point3D.sub(lookAt, position);
		z.normalize();
		Point3D x = Point3D.cross(up, z);
		x.normalize();
		Point3D y = Point3D.cross(z, x);
		Matrix result = Matrix.createIdentity();
		result.M11 = x.x;
		result.M21 = x.y;
		result.M31 = x.z;
		result.M12 = y.x;
		result.M22 = y.y;
		result.M32 = y.z;
		result.M13 = z.x;
		result.M23 = z.y;
		result.M33 = z.z;
		result.M41 = -Point3D.dot(x, position);
		result.M42 = -Point3D.dot(y, position);
		result.M43 = -Point3D.dot(z, position);
	}
}
