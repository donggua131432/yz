package com.yz.model.common;

public class PageCondition {

	private int start;
	private int length = 10;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		setLength(length, 10);
	}
	
	private void setLength(int length, int _default) {
		if(this.length == 0)
			this.length = _default;
		else 
			this.length = length;
	}
}
