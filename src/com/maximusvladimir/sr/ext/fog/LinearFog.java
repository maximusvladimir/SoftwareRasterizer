package com.maximusvladimir.sr.ext.fog;

public class LinearFog implements IFogEquation {
	private float _start = 3;
	private float _end = 7;
	private float _den = 1 / (_end - _start);
	public LinearFog() {
		
	}
	
	public LinearFog(float start, float end) {
		_start = start;
		_end = end;
	}
	
	public float getEnd() {
		return _end;
	}
	
	public float getStart() {
		return _start;
	}
	
	public void setStart(float start) {
		_start = start;
		_den = 1 / (_end - _start);
	}
	
	public void setEnd(float end) {
		_end = end;
		_den = 1 / (_end - _start);
	}
	
	public float calculateFog(float z) {
		float res = (z - getEnd()) * _den;
		if (res < 0)
			res = 0;
		if (res > 1)
			res = 1;
		return res;
	}
}
