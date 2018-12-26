package com.yz.model.educational;

import java.util.ArrayList;
import java.util.List;

public class CourseExcel {
	private String courseName;
	private String courseType;
	private String year;
	private String mappingId;
	private String mappingName;
	private String unvsName;
	private String pfsnLevel;
	private String pfsnName;
	private String grade;
	private String semester;
	private String remark;
	private List<InformationExcel> infoList = new ArrayList<InformationExcel>();
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof CourseExcel) {
			CourseExcel c = (CourseExcel) obj;
			if (courseName.equals(c.getCourseName()) && courseType.equals(c.getCourseType())
					&& year.equals(c.getYear()))
				return true;
			return false;
		}
		return super.equals(obj);
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester == null ? null : semester.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName == null ? null : courseName.trim();
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType == null ? null : courseType.trim();
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year == null ? null : year.trim();
	}
	
	public String getMappingId() {
		return mappingId;
	}
	
	public void setMappingId(String mappingId) {
		this.mappingId = mappingId == null ? null : mappingId.trim();
	}

	public String getMappingName() {
		return mappingName;
	}

	public void setMappingName(String mappingName) {
		this.mappingName = mappingName == null ? null : mappingName.trim();
	}

	public List<InformationExcel> getInfoList() {
		return infoList;
	}

	public void setInfoList(List<InformationExcel> infoList) {
		this.infoList = infoList;
	}

	

	public static class InformationExcel {
		private String mappingId;
		private String mappingName;


		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;

			if (obj instanceof InformationExcel) {
				InformationExcel t = (InformationExcel) obj;
				if (mappingName.equals(t.getMappingName()) && mappingId.equals(t.getMappingId()))
					return true;
				return false;
			}
			return super.equals(obj);
		}


		public String getMappingId() {
			return mappingId;
		}


		public void setMappingId(String mappingId) {
			this.mappingId = mappingId == null ? null : mappingId.trim();
		}


		public String getMappingName() {
			return mappingName;
		}


		public void setMappingName(String mappingName) {
			this.mappingName = mappingName == null ? null : mappingName.trim();
		}
	}
	
}
