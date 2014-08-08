package com.maximusvladimir.sr.math;

import com.maximusvladimir.sr.DisplayList;
import com.maximusvladimir.sr.Normal;
import com.maximusvladimir.sr.Point3D;
import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.Texture;
import com.maximusvladimir.sr.TextureCoord;

public class Triangle {
	public Point3D p1, p2,p3;
	public TextureCoord tc1,tc2,tc3;
	public RGB c1,c2,c3;
	public Normal n1,n2,n3;
	public Texture t;
	public Matrix mvp;
	public DisplayList list;
}
