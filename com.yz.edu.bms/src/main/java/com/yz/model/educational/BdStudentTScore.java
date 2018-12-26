package com.yz.model.educational;


import java.util.Date;

import com.yz.model.common.PubInfo;

public class BdStudentTScore extends PubInfo {
    private String tscoreId;

    private String learnId;

    private String stdId;

    private String semester;

    private String grade;

    private String courseName;

    private String courseId;

    private String score;
    
    private String totalmark;

    private String teacherId;

    private String teacher;

    private String isPass;
    
    private Date updateTime;
    
    private String ext1;
    
    private String ext2;
    
    private String ext3;
    
    public String getTscoreId() {
        return tscoreId;
    }

    public void setTscoreId(String tscoreId) {
        this.tscoreId = tscoreId == null ? null : tscoreId.trim();
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId == null ? null : learnId.trim();
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId == null ? null : stdId.trim();
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester == null ? null : semester.trim();
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName == null ? null : courseName.trim();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId == null ? null : courseId.trim();
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId == null ? null : teacherId.trim();
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher == null ? null : teacher.trim();
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass == null ? null : isPass.trim();
    }

	public String getTotalmark() {
		return totalmark;
	}

	public void setTotalmark(String totalmark) {
		this.totalmark = totalmark == null ? null : totalmark.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	
    
    
}