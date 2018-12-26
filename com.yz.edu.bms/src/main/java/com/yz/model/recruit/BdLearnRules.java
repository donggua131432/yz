package com.yz.model.recruit;

public class BdLearnRules {
    private String learnId;

    private String recruit;

    private String recruitDpId;
    
    private String recruitCampusId;
    
    private String recruitCampusManager;

    private String tutor;

    private String tutorDpId;
    
    private String tutorCampusId;
    
    private String tutorCampusManager;

    private String assignFlag;

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId == null ? null : learnId.trim();
    }

    public String getRecruit() {
        return recruit;
    }

    public void setRecruit(String recruit) {
        this.recruit = recruit == null ? null : recruit.trim();
    }

    public String getRecruitDpId() {
        return recruitDpId;
    }

    public void setRecruitDpId(String recruitDpId) {
        this.recruitDpId = recruitDpId == null ? null : recruitDpId.trim();
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor == null ? null : tutor.trim();
    }

    public String getTutorDpId() {
        return tutorDpId;
    }

    public void setTutorDpId(String tutorDpId) {
        this.tutorDpId = tutorDpId == null ? null : tutorDpId.trim();
    }

	public String getRecruitCampusId() {
		return recruitCampusId;
	}

	public void setRecruitCampusId(String recruitCampusId) {
		this.recruitCampusId = recruitCampusId == null ? null : recruitCampusId.trim();
	}

	public String getTutorCampusId() {
		return tutorCampusId;
	}

	public void setTutorCampusId(String tutorCampusId) {
		this.tutorCampusId = tutorCampusId == null ? null : tutorCampusId.trim();
	}

	public String getRecruitCampusManager() {
		return recruitCampusManager;
	}

	public void setRecruitCampusManager(String recruitCampusManager) {
		this.recruitCampusManager = recruitCampusManager == null ? null : recruitCampusManager.trim();
	}

	public String getTutorCampusManager() {
		return tutorCampusManager;
	}

	public void setTutorCampusManager(String tutorCampusManager) {
		this.tutorCampusManager = tutorCampusManager == null ? null : tutorCampusManager.trim();
	}

    public String getAssignFlag() {
        return assignFlag;
    }

    public void setAssignFlag(String assignFlag) {
        this.assignFlag = assignFlag;
    }
}