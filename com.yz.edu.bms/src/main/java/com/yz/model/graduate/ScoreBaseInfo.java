package com.yz.model.graduate;

import java.io.Serializable;

/**
 * 成绩的基础信息
 * @author lx
 * @date 2017年7月14日 上午9:53:55
 */
public class ScoreBaseInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5850342250500974668L;
	private String semester;          //学期
	private String courseName;        //课程
	private String score;             //成绩
	private String isPass;            //是否通过
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getIsPass() {
		return isPass;
	}
	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}
}
