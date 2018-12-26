package com.yz.model.common;

public class TbQueryInfo extends PageCondition {

	private String textbookName;
	private String publisher;
	private String textbookType;
	
	private String courseId;
	
	private String[] selectedTbIds;
	

	public String getTextbookName() {
		return textbookName;
	}
	public void setTextbookName(String textbookName) {
		this.textbookName = textbookName;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getTextbookType() {
		return textbookType;
	}
	public void setTextbookType(String textbookType) {
		this.textbookType = textbookType;
	}
	public String[] getSelectedTbIds() {
		return selectedTbIds;
	}
	public void setSelectedTbIds(String[] selectedTbIds) {
		this.selectedTbIds = selectedTbIds;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
}
