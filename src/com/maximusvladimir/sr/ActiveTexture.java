package com.maximusvladimir.sr;

public class ActiveTexture extends Operation {
	private int _tid;
	public ActiveTexture(int tid) {
		id = 7;
		_tid = tid;
	}
	
	public int getTextureId() {
		return _tid;
	}
}
