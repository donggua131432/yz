package com.yz.model.wechat;

import com.yz.model.common.PubInfo;

public class GwWechatMenu extends PubInfo {
	private String id;

	private String menuTitle;

	private String pId;

	private String menuContent;

	private String weight;

	private String belongPublic;

	private String eventType;

	private String isAllow;

	private String pName;

	private String pubName;

	public String getPubName() {
		return pubName;
	}

	public void setPubName(String pubName) {
		this.pubName = pubName;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle == null ? null : menuTitle.trim();
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId == null ? null : pId.trim();
	}

	public String getMenuContent() {
		return menuContent;
	}

	public void setMenuContent(String menuContent) {
		this.menuContent = menuContent == null ? null : menuContent.trim();
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight == null ? null : weight.trim();
	}

	public String getBelongPublic() {
		return belongPublic;
	}

	public void setBelongPublic(String belongPublic) {
		this.belongPublic = belongPublic == null ? null : belongPublic.trim();
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType == null ? null : eventType.trim();
	}

}