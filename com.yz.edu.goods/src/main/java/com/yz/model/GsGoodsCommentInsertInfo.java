package com.yz.model;

import java.io.Serializable;

/**
 * 活动商品评论插入信息
 * @author lx
 * @date 2017年7月25日 下午4:04:20
 */
public class GsGoodsCommentInsertInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8017758729212877408L;
	private String commentId;
	private String salesId;
	private String commentTitle;
	private String commentContent;
	private String userId;
	private String headImgUrl;
	private String userName;
	private String mobile;
	private String remark;
	
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getCommentTitle() {
		return commentTitle;
	}
	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
}
