package com.yz.model.educational;

public class BdStudentTScoreExcel {
	private String idCard;
	private String stdName;
	private String stdNo;
	private String schoolRoll;
	private String grade;
	private String unvsName;
	private String unvsId;
	private String pfsnLevel;
	private String pfsnName;
	private String pfsnId;
	private String year;
	private String courseId;
	private String courseName;
	private String semester;
	private String score;
	private String totalmark;
	private String teacherId;
	private String teacher;
	private String isPass;
	
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard == null ? null : idCard.trim();
	}
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName == null ? null : stdName.trim();
	}
	public String getSchoolRoll() {
		return schoolRoll;
	}
	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
	}
	public String getUnvsName() {
		return unvsName;
	}
	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName == null ? null : unvsName.trim();
	}
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel == null ? null : pfsnLevel.trim();
	}
	public String getPfsnName() {
		return pfsnName;
	}
	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName == null ? null : pfsnName.trim();
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year == null ? null : year.trim();
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId == null ? null : courseId.trim();
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName == null ? null : courseName.trim();
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester == null ? null : semester.trim();
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score == null ? null : score.trim();
	}
	
	public String getTotalmark() {
		return totalmark;
	}
	public void setTotalmark(String totalmark) {
		this.totalmark = totalmark == null ? null : totalmark.trim();
	}
	public String getUnvsId() {
		return unvsId;
	}
	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}
	public String getPfsnId() {
		return pfsnId;
	}
	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
	}
	
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	
	public String getIsPass() {
		return isPass;
	}
	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}
	
	public String getStdNo() {
		return stdNo;
	}
	public void setStdNo(String stdNo) {
		this.stdNo = stdNo;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof BdStudentTScoreExcel) {
			BdStudentTScoreExcel c = (BdStudentTScoreExcel) obj;
			if ((idCard.equals(c.getIdCard())||schoolRoll.equals(c.getSchoolRoll())) && year.equals(c.getYear())
					&& courseId.equals(c.getCourseId())&& semester.equals(c.getSemester()))
				return true;
			return false;
		}
		return super.equals(obj);
	}
	
		
	
	
}
