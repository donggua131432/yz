package com.yz.model.transfer;

import java.util.List;

public class BdStudentRollModify {
	private String modifyId;

	private String learnId;

	private String stdId;

	private String crId;

	private String taName;

	private String ntaName;

	private String learnStage;

	private String recruitType;

	private String npfsnName;

	private String nunvsName;

	private String reason;

	private String grade;

	private String empName;

	private String stdStage;

	private String bcruptime;

	private String unvsName;
	
	private String bScholarship;

	private String pfsnName;

	private String pfsnCode;

	private String pfsnLevel;

	private String modifyType;

	private String checkType;

	private String checkOrder;

	private String stdName;

	private String sex;

	private String idType;

	private String nation;

	private String idCard;

	private String unvsId;

	private String taId;

	private String pfsnId;

	private String scholarship;

	private String newStdName;

	private String newSex;

	private String newIdType;

	private String newNation;

	private String newIdCard;

	private String newUnvsId;
	
	private String npfsnLevel;

	private String newTaId;

	private String newPfsnId;

	private String newScholarship;

	private String isDel;

	private String updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private String createTime;

	private String createUser;

	private String ext1;

	private String ext2;

	private String ext3;

	private List<BdRollCheckRecord> check;

	private String inclusionStatus;

	private String score;



	public String getNpfsnLevel() {
		return npfsnLevel;
	}

	public void setNpfsnLevel(String npfsnLevel) {
		this.npfsnLevel = npfsnLevel;
	}

	public String getbScholarship() {
		return bScholarship;
	}

	public void setbScholarship(String bScholarship) {
		this.bScholarship = bScholarship;
	}

	public List<BdRollCheckRecord> getCheck() {
		return check;
	}

	public String getLearnStage() {
		return learnStage;
	}

	public void setLearnStage(String learnStage) {
		this.learnStage = learnStage;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public void setCheck(List<BdRollCheckRecord> check) {
		this.check = check;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getReason() {
		return reason;
	}

	public String getCrId() {
		return crId;
	}

	public void setCrId(String crId) {
		this.crId = crId;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getBcruptime() {
		return bcruptime;
	}

	public void setBcruptime(String bcruptime) {
		this.bcruptime = bcruptime;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getPfsnCode() {
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}

	public String getNtaName() {
		return ntaName;
	}

	public void setNtaName(String ntaName) {
		this.ntaName = ntaName;
	}

	public String getNpfsnName() {
		return npfsnName;
	}

	public void setNpfsnName(String npfsnName) {
		this.npfsnName = npfsnName;
	}

	public String getNunvsName() {
		return nunvsName;
	}

	public void setNunvsName(String nunvsName) {
		this.nunvsName = nunvsName;
	}

	public String getCheckOrder() {
		return checkOrder;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public void setCheckOrder(String checkOrder) {
		this.checkOrder = checkOrder;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId == null ? null : modifyId.trim();
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

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType == null ? null : modifyType.trim();
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName == null ? null : stdName.trim();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType == null ? null : idType.trim();
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation == null ? null : nation.trim();
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard == null ? null : idCard.trim();
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId == null ? null : unvsId.trim();
	}

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId == null ? null : taId.trim();
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId == null ? null : pfsnId.trim();
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship == null ? null : scholarship.trim();
	}

	public String getNewStdName() {
		return newStdName;
	}

	public void setNewStdName(String newStdName) {
		this.newStdName = newStdName == null ? null : newStdName.trim();
	}

	public String getNewSex() {
		return newSex;
	}

	public void setNewSex(String newSex) {
		this.newSex = newSex == null ? null : newSex.trim();
	}

	public String getNewIdType() {
		return newIdType;
	}

	public void setNewIdType(String newIdType) {
		this.newIdType = newIdType == null ? null : newIdType.trim();
	}

	public String getNewNation() {
		return newNation;
	}

	public void setNewNation(String newNation) {
		this.newNation = newNation == null ? null : newNation.trim();
	}

	public String getNewIdCard() {
		return newIdCard;
	}

	public void setNewIdCard(String newIdCard) {
		this.newIdCard = newIdCard == null ? null : newIdCard.trim();
	}

	public String getNewUnvsId() {
		return newUnvsId;
	}

	public void setNewUnvsId(String newUnvsId) {
		this.newUnvsId = newUnvsId == null ? null : newUnvsId.trim();
	}

	public String getNewTaId() {
		return newTaId;
	}

	public void setNewTaId(String newTaId) {
		this.newTaId = newTaId == null ? null : newTaId.trim();
	}

	public String getNewPfsnId() {
		return newPfsnId;
	}

	public void setNewPfsnId(String newPfsnId) {
		this.newPfsnId = newPfsnId == null ? null : newPfsnId.trim();
	}

	public String getNewScholarship() {
		return newScholarship;
	}

	public void setNewScholarship(String newScholarship) {
		this.newScholarship = newScholarship == null ? null : newScholarship.trim();
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel == null ? null : isDel.trim();
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim();
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId == null ? null : updateUserId.trim();
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId == null ? null : createUserId.trim();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1 == null ? null : ext1.trim();
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2 == null ? null : ext2.trim();
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3 == null ? null : ext3.trim();
	}

	public String getInclusionStatus() {
		return inclusionStatus;
	}

	public void setInclusionStatus(String inclusionStatus) {
		this.inclusionStatus = inclusionStatus;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
}