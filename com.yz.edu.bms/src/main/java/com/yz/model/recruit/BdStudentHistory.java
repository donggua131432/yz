package com.yz.model.recruit;

import com.yz.model.common.PubInfo;

public class BdStudentHistory extends PubInfo {

	private String learnId;
	private String stdId;
	private String unvsName;
	private String oldProvinceCode;
	private String oldCityCode;
	private String oldDistrictCode;
	private String adminssionTime;
	private String graduateTime;
	private String profession;
	private String diploma;
	private String edcsSystem;
	private String isOpenUnvs;
	private String unvsType;
	private String edcsType;
	private String recruitType;

	private String subject;
	private String subjectCategory;
	private String studyType;
	private String materialType;
	private String materialCode;

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId== null ? null : stdId.trim();
	}
	
	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName == null ? null : unvsName.trim();
	}

	public String getOldProvinceCode() {
		return oldProvinceCode;
	}

	public void setOldProvinceCode(String oldProvinceCode) {
		this.oldProvinceCode = oldProvinceCode == null ? null : oldProvinceCode.trim();
	}

	public String getOldCityCode() {
		return oldCityCode;
	}

	public void setOldCityCode(String oldCityCode) {
		this.oldCityCode = oldCityCode == null ? null : oldCityCode.trim();
	}

	public String getOldDistrictCode() {
		return oldDistrictCode;
	}

	public void setOldDistrictCode(String oldDistrictCode) {
		this.oldDistrictCode = oldDistrictCode == null ? null : oldDistrictCode.trim();
	}

	public String getAdminssionTime() {
		return adminssionTime;
	}

	public void setAdminssionTime(String adminssionTime) {
		this.adminssionTime = adminssionTime == null ? null : adminssionTime.trim();
	}

	public String getGraduateTime() {
		return graduateTime;
	}

	public void setGraduateTime(String graduateTime) {
		this.graduateTime = graduateTime == null ? null : graduateTime.trim();
	}

	public String getDiploma() {
		return diploma;
	}

	public void setDiploma(String diploma) {
		this.diploma = diploma == null ? null : diploma.trim();
	}

	public String getEdcsSystem() {
		return edcsSystem;
	}

	public void setEdcsSystem(String edcsSystem) {
		this.edcsSystem = edcsSystem == null ? null : edcsSystem.trim();
	}

	public String getIsOpenUnvs() {
		return isOpenUnvs;
	}

	public void setIsOpenUnvs(String isOpenUnvs) {
		this.isOpenUnvs = isOpenUnvs == null ? null : isOpenUnvs.trim();
	}

	public String getUnvsType() {
		return unvsType;
	}

	public void setUnvsType(String unvsType) {
		this.unvsType = unvsType == null ? null : unvsType.trim();
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession == null ? null : profession.trim();
	}

	public String getEdcsType() {
		return edcsType;
	}

	public void setEdcsType(String edcsType) {
		this.edcsType = edcsType == null ? null : edcsType.trim();
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType == null ? null : recruitType.trim();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubjectCategory() {
		return subjectCategory;
	}

	public void setSubjectCategory(String subjectCategory) {
		this.subjectCategory = subjectCategory;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
}
