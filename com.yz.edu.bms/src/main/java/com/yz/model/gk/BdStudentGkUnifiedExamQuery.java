package com.yz.model.gk;

/**
 * 国开统考查询信息
 * @author lx
 * @date 2018年6月6日 上午10:09:50
 */
public class BdStudentGkUnifiedExamQuery {

	private String title;
	private String ifShow;
	private String testSubject;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIfShow() {
		return ifShow;
	}
	public void setIfShow(String ifShow) {
		this.ifShow = ifShow;
	}
	public String getTestSubject() {
		return testSubject;
	}
	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
	}
}
