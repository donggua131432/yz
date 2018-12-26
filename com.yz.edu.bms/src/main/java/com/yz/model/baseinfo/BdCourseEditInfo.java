package com.yz.model.baseinfo;

import java.util.List;

public class BdCourseEditInfo {

	private String[] thpIds;
	private String[] courseResources;
	private String[] textbookIds;
	
	private String[] delResourceIds;
	
	private String[] delResourceUrls;
	
	private List<CourseResourceEdit> courseResource;
	
	public String[] getThpIds() {
		return thpIds;
	}
	public void setThpIds(String[] thpIds) {
		this.thpIds = thpIds;
	}
	public String[] getCourseResources() {
		return courseResources;
	}
	public void setCourseResources(String[] courseResources) {
		this.courseResources = courseResources;
	}
	public String[] getTextbookIds() {
		return textbookIds;
	}
	public void setTextbookIds(String[] textbookIds) {
		this.textbookIds = textbookIds;
	}
	
	public List<CourseResourceEdit> getCourseResource() {
		return courseResource;
	}
	public void setCourseResource(List<CourseResourceEdit> courseResource) {
		this.courseResource = courseResource;
	}
	public String[] getDelResourceIds() {
		return delResourceIds;
	}
	public void setDelResourceIds(String[] delResourceIds) {
		this.delResourceIds = delResourceIds;
	}
	public String[] getDelResourceUrls() {
		return delResourceUrls;
	}
	public void setDelResourceUrls(String[] delResourceUrls) {
		this.delResourceUrls = delResourceUrls;
	}
}
