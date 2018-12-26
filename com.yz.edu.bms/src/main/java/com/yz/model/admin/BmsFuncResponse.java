package com.yz.model.admin;

import java.util.ArrayList;
import java.util.List;

public class BmsFuncResponse extends BmsFunc {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4209111720052175194L;
	
	
	private List<BmsFuncResponse> funcs = new ArrayList<BmsFuncResponse>();

	public List<BmsFuncResponse> getFuncs() {
		return funcs;
	}

	public void setFuncs(List<BmsFuncResponse> funcs) {
		this.funcs = funcs;
	}

}
