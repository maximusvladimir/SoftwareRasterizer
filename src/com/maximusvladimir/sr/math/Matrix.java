package com.maximusvladimir.sr.math;

import com.maximusvladimir.sr.Operation;
import com.maximusvladimir.sr.Point3D;

public class Matrix extends Operation {
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

	public float Znear = 0;
	public float Zfar = 0;

	public Matrix() {
		id = 5;
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
		temp.M11 = (left.M11 * right.M11) + (left.M12 * right.M21)
				+ (left.M13 * right.M31) + (left.M14 * right.M41);
		temp.M12 = (left.M11 * right.M12) + (left.M12 * right.M22)
				+ (left.M13 * right.M32) + (left.M14 * right.M42);
		temp.M13 = (left.M11 * right.M13) + (left.M12 * right.M23)
				+ (left.M13 * right.M33) + (left.M14 * right.M43);
		temp.M14 = (left.M11 * right.M14) + (left.M12 * right.M24)
				+ (left.M13 * right.M34) + (left.M14 * right.M44);
		temp.M21 = (left.M21 * right.M11) + (left.M22 * right.M21)
				+ (left.M23 * right.M31) + (left.M24 * right.M41);
		temp.M22 = (left.M21 * right.M12) + (left.M22 * right.M22)
				+ (left.M23 * right.M32) + (left.M24 * right.M42);
		temp.M23 = (left.M21 * right.M13) + (left.M22 * right.M23)
				+ (left.M23 * right.M33) + (left.M24 * right.M43);
		temp.M24 = (left.M21 * right.M14) + (left.M22 * right.M24)
				+ (left.M23 * right.M34) + (left.M24 * right.M44);
		temp.M31 = (left.M31 * right.M11) + (left.M32 * right.M21)
				+ (left.M33 * right.M31) + (left.M34 * right.M41);
		temp.M32 = (left.M31 * right.M12) + (left.M32 * right.M22)
				+ (left.M33 * right.M32) + (left.M34 * right.M42);
		temp.M33 = (left.M31 * right.M13) + (left.M32 * right.M23)
				+ (left.M33 * right.M33) + (left.M34 * right.M43);
		temp.M34 = (left.M31 * right.M14) + (left.M32 * right.M24)
				+ (left.M33 * right.M34) + (left.M34 * right.M44);
		temp.M41 = (left.M41 * right.M11) + (left.M42 * right.M21)
				+ (left.M43 * right.M31) + (left.M44 * right.M41);
		temp.M42 = (left.M41 * right.M12) + (left.M42 * right.M22)
				+ (left.M43 * right.M32) + (left.M44 * right.M42);
		temp.M43 = (left.M41 * right.M13) + (left.M42 * right.M23)
				+ (left.M43 * right.M33) + (left.M44 * right.M43);
		temp.M44 = (left.M41 * right.M14) + (left.M42 * right.M24)
				+ (left.M43 * right.M34) + (left.M44 * right.M44);
		return temp;
	}

	public String toString() {
		return String
				.format("[M11:%s M12:%s M13:%s M14:%s]\n[M21:%s M22:%s M23:%s M24:%s]\n[M31:%s M32:%s M33:%s M34:%s]\n[M41:%s M42:%s M43:%s M44:%s]",
						M11, M12, M13, M14, M21, M22, M23, M24, M31, M32, M33,
						M34, M41, M42, M43, M44);
	}

	public void setToTranslation(Point3D t) {
		M11 = 1.0f;
		M22 = 1.0f;
		M33 = 1.0f;
		M44 = 1.0f;
		M41 = t.x;
		M42 = t.y;
		M43 = t.z;
	}

	public void setToPerspective(float width, float height, float znear,
			float zfar) {
		Zfar = zfar;
		Znear = znear;
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
		M11 = 1.0f;
		M22 = 1.0f;
		M33 = 1.0f;
		M44 = 1.0f;
		M11 = x.x;
		M21 = x.y;
		M31 = x.z;
		M12 = y.x;
		M22 = y.y;
		M32 = y.z;
		M13 = z.x;
		M23 = z.y;
		M33 = z.z;
		M41 = -Point3D.dot(x, position);
		M42 = -Point3D.dot(y, position);
		M43 = -Point3D.dot(z, position);
	}
	
	public Point3D antiProj(Point3D o) {
		float x = o.x;
		float y = o.y;
		float z = o.z;
		final float l_w = 1f / (x * M41 + y * M42 + z * M43 + M44);
		return new Point3D((x * M11 + y * M12 + z * M13 + M14) * l_w, (x
			* M21 + y * M22 + z * M23 + M24)
			* l_w, (x * M31 + y * M32 + z * M33 + M34) * l_w);
	}

	public void inverse() {
		float l_det = M41 * M32 * M23 * M14 - M31 * M42 * M23 * M14 - M41 * M22
				* M33 * M14 + M21 * M42 * M33 * M14 + M31 * M22 * M43 * M14
				- M21 * M32 * M43 * M14 - M41 * M32 * M13 * M24 + M31 * M42
				* M13 * M24 + M41 * M12 * M33 * M24 - M11 * M42 * M33 * M24
				- M31 * M12 * M43 * M24 + M11 * M32 * M43 * M24 + M41 * M22
				* M13 * M34 - M21 * M42 * M13 * M34 - M41 * M12 * M23 * M34
				+ M11 * M42 * M23 * M34 + M21 * M12 * M43 * M34 - M11 * M22
				* M43 * M34 - M31 * M22 * M13 * M44 + M21 * M32 * M13 * M44
				+ M31 * M12 * M23 * M44 - M11 * M32 * M23 * M44 - M21 * M12
				* M33 * M44 + M11 * M22 * M33 * M44;
		if (l_det == 0)
			return;
		//if (l_det == 0f)
			//throw new RuntimeException("non-invertible matrix");
		float inv_det = 1.0f / l_det;
		float tmpM11 = M23 * M34 * M42 - M24 * M33 * M42 + M24 * M32 * M43
				- M22 * M34 * M43 - M23 * M32 * M44 + M22 * M33 * M44;
		float tmpM12 = M14 * M33 * M42 - M13 * M34 * M42 - M14 * M32 * M43
				+ M12 * M34 * M43 + M13 * M32 * M44 - M12 * M33 * M44;
		float tmpM13 = M13 * M24 * M42 - M14 * M23 * M42 + M14 * M22 * M43
				- M12 * M24 * M43 - M13 * M22 * M44 + M12 * M23 * M44;
		float tmpM14 = M14 * M23 * M32 - M13 * M24 * M32 - M14 * M22 * M33
				+ M12 * M24 * M33 + M13 * M22 * M34 - M12 * M23 * M34;
		float tmpM21 = M24 * M33 * M41 - M23 * M34 * M41 - M24 * M31 * M43
				+ M21 * M34 * M43 + M23 * M31 * M44 - M21 * M33 * M44;
		float tmpM22 = M13 * M34 * M41 - M14 * M33 * M41 + M14 * M31 * M43
				- M11 * M34 * M43 - M13 * M31 * M44 + M11 * M33 * M44;
		float tmpM23 = M14 * M23 * M41 - M13 * M24 * M41 - M14 * M21 * M43
				+ M11 * M24 * M43 + M13 * M21 * M44 - M11 * M23 * M44;
		float tmpM24 = M13 * M24 * M31 - M14 * M23 * M31 + M14 * M21 * M33
				- M11 * M24 * M33 - M13 * M21 * M34 + M11 * M23 * M34;
		float tmpM31 = M22 * M34 * M41 - M24 * M32 * M41 + M24 * M31 * M42
				- M21 * M34 * M42 - M22 * M31 * M44 + M21 * M32 * M44;
		float tmpM32 = M14 * M32 * M41 - M12 * M34 * M41 - M14 * M31 * M42
				+ M11 * M34 * M42 + M12 * M31 * M44 - M11 * M32 * M44;
		float tmpM33 = M12 * M24 * M41 - M14 * M22 * M41 + M14 * M21 * M42
				- M11 * M24 * M42 - M12 * M21 * M44 + M11 * M22 * M44;
		float tmpM34 = M14 * M22 * M31 - M12 * M24 * M31 - M14 * M21 * M32
				+ M11 * M24 * M32 + M12 * M21 * M34 - M11 * M22 * M34;
		float tmpM41 = M23 * M32 * M41 - M22 * M33 * M41 - M23 * M31 * M42
				+ M21 * M33 * M42 + M22 * M31 * M43 - M21 * M32 * M43;
		float tmpM42 = M12 * M33 * M41 - M13 * M32 * M41 + M13 * M31 * M42
				- M11 * M33 * M42 - M12 * M31 * M43 + M11 * M32 * M43;
		float tmpM43 = M13 * M22 * M41 - M12 * M23 * M41 - M13 * M21 * M42
				+ M11 * M23 * M42 + M12 * M21 * M43 - M11 * M22 * M43;
		float tmpM44 = M12 * M23 * M31 - M13 * M22 * M31 + M13 * M21 * M32
				- M11 * M23 * M32 - M12 * M21 * M33 + M11 * M22 * M33;
		M11 = tmpM11 * inv_det;
		M12 = tmpM12 * inv_det;
		M13 = tmpM13 * inv_det;
		M14 = tmpM14 * inv_det;
		M21 = tmpM21 * inv_det;
		M22 = tmpM22 * inv_det;
		M23 = tmpM23 * inv_det;
		M24 = tmpM24 * inv_det;
		M31 = tmpM31 * inv_det;
		M32 = tmpM32 * inv_det;
		M33 = tmpM33 * inv_det;
		M34 = tmpM34 * inv_det;
		M41 = tmpM41 * inv_det;
		M42 = tmpM42 * inv_det;
		M43 = tmpM43 * inv_det;
		M44 = tmpM44 * inv_det;
	}
}
