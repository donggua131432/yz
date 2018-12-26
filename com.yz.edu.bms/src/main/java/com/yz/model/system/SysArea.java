package com.yz.model.system;

public class SysArea {

	private String id;
	private String pId;
	private String name;
	private boolean open;
	private boolean isParent;
	private boolean isShowCheck;
	private boolean isChecked;
	private String flag;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public void setShowCheck(boolean isShowCheck) {
		this.isShowCheck = isShowCheck;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
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

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean getIsShowCheck() {
		return isShowCheck;
	}

	public void setIsShowCheck(boolean isShowCheck) {
		this.isShowCheck = isShowCheck;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
