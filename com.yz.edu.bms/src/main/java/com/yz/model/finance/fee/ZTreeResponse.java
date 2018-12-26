package com.yz.model.finance.fee;

public class ZTreeResponse {

	private String id;
	private int pId = 0;
	private String name;
	private boolean open = false;
	private boolean isParent = false;
	private boolean isShowCheck = false;
	private boolean isChecked = false;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isShowCheck() {
		return isShowCheck;
	}

	public void setShowCheck(boolean isShowCheck) {
		this.isShowCheck = isShowCheck;
	}

}
