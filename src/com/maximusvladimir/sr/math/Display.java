package com.maximusvladimir.sr.math;

import java.util.ArrayList;

import com.maximusvladimir.sr.Normal;
import com.maximusvladimir.sr.Operation;
import com.maximusvladimir.sr.Point3D;
import com.maximusvladimir.sr.RGB;
import com.maximusvladimir.sr.Texture;
import com.maximusvladimir.sr.TextureCoord;

public class Display {
	public static final void convertOperationsToTriangles(ArrayList<Operation> _operations,ArrayList<Triangle> _triangles) {
		Triangle t = new Triangle();
		for (int i = 0; i < _operations.size(); i++) {
			Operation op = _operations.get(i);
			if (op.id == 0) {
				// Vertex
				if (t.p1 == null)
					t.p1 = (Point3D)op;
				else if (t.p2 == null)
					t.p2 = (Point3D)op;
				else {
					t.p3 = (Point3D)op;
					_triangles.add(t);
					t = new Triangle();
				}
			}
			else if (op.id == 1) {
				// Color
				if (t.c1 == null)
					t.c1 = (RGB)op;
				else if (t.c2 == null)
					t.c2 = (RGB)op;
				else
					t.c3 = (RGB)op;
			}
			else if (op.id == 2) {
				// Texture Coord
				if (t.tc1 == null)
					t.tc1 = (TextureCoord)op;
				else if (t.tc2 == null)
					t.tc2 = (TextureCoord)op;
				else
					t.tc3 = (TextureCoord)op;
			}
			else if (op.id == 3) {
				// Normal
				if (t.n1 == null)
					t.n1 = (Normal)op;
				else if (t.n2 == null)
					t.n2 = (Normal)op;
				else
					t.n3 = (Normal)op;
			}
			else if (op.id == 4) {
				t.t = (Texture)op;
			}
		}
	}
}
