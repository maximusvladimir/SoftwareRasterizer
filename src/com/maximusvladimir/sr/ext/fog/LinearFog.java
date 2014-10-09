package com.maximusvladimir.sr.ext.fog;

public class LinearFog implements IFogEquation {
	private float _start = 5;
	private float _end = 20;
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
	}
	
	public void setEnd(float end) {
		_end = end;
	}
	
	public float calculateFog(float z) {
		return 1-z;//(getEnd() - z) / (getEnd() - getStart());
	}
}
