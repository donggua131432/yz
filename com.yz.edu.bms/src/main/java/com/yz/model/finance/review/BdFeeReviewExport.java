package com.yz.model.finance.review;

import java.util.List;

public class BdFeeReviewExport {

	private String stdId;
	private String serialNo;
	private String recruit;
	private String recruitDepart;
	private String recruitCampus;
	private String unvsName;
	private String pfsnName;
	private String pfsnCode;
	private String pfsnLevel;
	private String grade;
	private String master;
	private String stdName;
	private String idCard;
	private String stdNo;
	private String mobile;
	private String taName;
	private String scholarship;
	private String stdStage;
	private String hasRoll;
	private String orderNo;
	private String isAllot;
	private String feeName;
	private String offerName;
	private List<String> couponName;
	private String couponNames;
	private String inclusionStatus;
	private String score;
	private String homeCampusName;

	private String delay;

	private String totalPayable = "0.00";
	private String totalPaid = "0.00";

	private BdFeeReviewTuition tutor;

	private String tutorCheck;
	private String tutorPayTime;
	private String tutorYear;
	private String tutorMonth;
	private String tutorPayable = "0.00";
	private String tutorPaid = "0.00";

	private BdFeeReviewTuition firstBook;
	private String firstBookPayable = "0.00";
	private String firstBookPaid = "0.00";

	private BdFeeReviewTuition first;
	private String firstPayable = "0.00";
	private String firstPaid = "0.00";

	private String firstOffer = "0.00";
	private String firstIsPayUp = "0.00";

	private BdFeeReviewTuition firstNet;
	private String firstNetPayable = "0.00";
	private String firstNetPaid = "0.00";

	private BdFeeReviewTuition secondNet;
	private String secondNetPayable = "0.00";
	private String secondNetPaid = "0.00";

	private BdFeeReviewTuition thirdNet;
	private String thirdNetPayable = "0.00";
	private String thirdNetPaid = "0.00";

	private BdFeeReviewTuition fourNet;
	private String fourNetPayable = "0.00";
	private String fourNetPaid = "0.00";

	private List<BdFeeReviewTuition> other;
	private String otherPayable = "0.00";
	private String otherPaid = "0.00";

	private BdFeeReviewTuition secondBook;
	private String secondBookPayable = "0.00";
	private String secondBookPaid = "0.00";

	private BdFeeReviewTuition second;
	private String secondPayable = "0.00";
	private String secondPaid = "0.00";
	private String secondOffer = "0.00";
	private String secondIsPayUp;

	private BdFeeReviewTuition thirdBook;
	private String thirdBookPayable = "0.00";
	private String thirdBookPaid = "0.00";

	private BdFeeReviewTuition third;
	private String thirdPayable = "0.00";
	private String thirdPaid = "0.00";
	private String thirdOffer = "0.00";
	private String thirdIsPayUp;

	private BdFeeReviewTuition fourBook;
	private String fourBookPayable = "0.00";
	private String fourBookPaid = "0.00";

	private BdFeeReviewTuition four;
	private String fourPayable = "0.00";
	private String fourPaid = "0.00";
	private String fourOffer = "0.00";
	private String fourIsPayUp;

	private String audit;
	private String number = "1";

	public String getIsAllot() {
		return isAllot;
	}

	public void setIsAllot(String isAllot) {
		this.isAllot = isAllot;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHomeCampusName() {
		return homeCampusName;
	}

	public void setHomeCampusName(String homeCampusName) {
		this.homeCampusName = homeCampusName;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getCouponNames() {
		return couponNames;
	}

	public void setCouponNames(String couponNames) {
		this.couponNames = couponNames;
	}

	public String getInclusionStatus() {
		return inclusionStatus;
	}

	public void setInclusionStatus(String inclusionStatus) {
		this.inclusionStatus = inclusionStatus;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public List<String> getCouponName() {
		return couponName;
	}

	public void setCouponName(List<String> couponName) {
		this.couponName = couponName;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public BdFeeReviewTuition getFourNet() {
		return fourNet;
	}

	public void setFourNet(BdFeeReviewTuition fourNet) {
		this.fourNet = fourNet;
	}

	public String getFourNetPayable() {
		return fourNetPayable;
	}

	public void setFourNetPayable(String fourNetPayable) {
		this.fourNetPayable = fourNetPayable;
	}

	public String getFourNetPaid() {
		return fourNetPaid;
	}

	public void setFourNetPaid(String fourNetPaid) {
		this.fourNetPaid = fourNetPaid;
	}

	public BdFeeReviewTuition getFourBook() {
		return fourBook;
	}

	public void setFourBook(BdFeeReviewTuition fourBook) {
		this.fourBook = fourBook;
	}

	public String getFourBookPayable() {
		return fourBookPayable;
	}

	public void setFourBookPayable(String fourBookPayable) {
		this.fourBookPayable = fourBookPayable;
	}

	public String getFourBookPaid() {
		return fourBookPaid;
	}

	public void setFourBookPaid(String fourBookPaid) {
		this.fourBookPaid = fourBookPaid;
	}

	public BdFeeReviewTuition getFour() {
		return four;
	}

	public void setFour(BdFeeReviewTuition four) {
		this.four = four;
	}

	public String getFourPayable() {
		return fourPayable;
	}

	public void setFourPayable(String fourPayable) {
		this.fourPayable = fourPayable;
	}

	public String getFourPaid() {
		return fourPaid;
	}

	public void setFourPaid(String fourPaid) {
		this.fourPaid = fourPaid;
	}

	public String getFourOffer() {
		return fourOffer;
	}

	public void setFourOffer(String fourOffer) {
		this.fourOffer = fourOffer;
	}

	public String getFourIsPayUp() {
		return fourIsPayUp;
	}

	public void setFourIsPayUp(String fourIsPayUp) {
		this.fourIsPayUp = fourIsPayUp;
	}

	public String getTotalPayable() {
		return totalPayable;
	}

	public void setTotalPayable(String totalPayable) {
		this.totalPayable = totalPayable;
	}

	public String getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(String totalPaid) {
		this.totalPaid = totalPaid;
	}

	public List<BdFeeReviewTuition> getOther() {
		return other;
	}

	public void setOther(List<BdFeeReviewTuition> other) {
		this.other = other;
	}

	public BdFeeReviewTuition getFirstNet() {
		return firstNet;
	}

	public void setFirstNet(BdFeeReviewTuition firstNet) {
		this.firstNet = firstNet;
	}

	public BdFeeReviewTuition getSecondNet() {
		return secondNet;
	}

	public void setSecondNet(BdFeeReviewTuition secondNet) {
		this.secondNet = secondNet;
	}

	public BdFeeReviewTuition getThirdNet() {
		return thirdNet;
	}

	public void setThirdNet(BdFeeReviewTuition thirdNet) {
		this.thirdNet = thirdNet;
	}

	public String getThirdNetPayable() {
		return thirdNetPayable;
	}

	public void setThirdNetPayable(String thirdNetPayable) {
		this.thirdNetPayable = thirdNetPayable;
	}

	public String getThirdNetPaid() {
		return thirdNetPaid;
	}

	public void setThirdNetPaid(String thirdNetPaid) {
		this.thirdNetPaid = thirdNetPaid;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getPfsnCode() {
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}

	public BdFeeReviewTuition getFirstBook() {
		return firstBook;
	}

	public void setFirstBook(BdFeeReviewTuition firstBook) {
		this.firstBook = firstBook;
	}

	public BdFeeReviewTuition getSecondBook() {
		return secondBook;
	}

	public void setSecondBook(BdFeeReviewTuition secondBook) {
		this.secondBook = secondBook;
	}

	public BdFeeReviewTuition getThirdBook() {
		return thirdBook;
	}

	public void setThirdBook(BdFeeReviewTuition thirdBook) {
		this.thirdBook = thirdBook;
	}

	public BdFeeReviewTuition getTutor() {
		return tutor;
	}

	public void setTutor(BdFeeReviewTuition tutor) {
		this.tutor = tutor;
	}

	public BdFeeReviewTuition getFirst() {
		return first;
	}

	public void setFirst(BdFeeReviewTuition first) {
		this.first = first;
	}

	public BdFeeReviewTuition getSecond() {
		return second;
	}

	public void setSecond(BdFeeReviewTuition second) {
		this.second = second;
	}

	public BdFeeReviewTuition getThird() {
		return third;
	}

	public void setThird(BdFeeReviewTuition third) {
		this.third = third;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRecruit() {
		return recruit;
	}

	public void setRecruit(String recruit) {
		this.recruit = recruit;
	}

	public String getRecruitDepart() {
		return recruitDepart;
	}

	public void setRecruitDepart(String recruitDepart) {
		this.recruitDepart = recruitDepart;
	}

	public String getRecruitCampus() {
		return recruitCampus;
	}

	public void setRecruitCampus(String recruitCampus) {
		this.recruitCampus = recruitCampus;
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

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getStdNo() {
		return stdNo;
	}

	public void setStdNo(String stdNo) {
		this.stdNo = stdNo;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}

	public String getTutorCheck() {
		return tutorCheck;
	}

	public void setTutorCheck(String tutorCheck) {
		this.tutorCheck = tutorCheck;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getTutorPayTime() {
		return tutorPayTime;
	}

	public void setTutorPayTime(String tutorPayTime) {
		this.tutorPayTime = tutorPayTime;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String getTutorYear() {
		return tutorYear;
	}

	public void setTutorYear(String tutorYear) {
		this.tutorYear = tutorYear;
	}

	public String getTutorMonth() {
		return tutorMonth;
	}

	public void setTutorMonth(String tutorMonth) {
		this.tutorMonth = tutorMonth;
	}

	public String getHasRoll() {
		return hasRoll;
	}

	public void setHasRoll(String hasRoll) {
		this.hasRoll = hasRoll;
	}

	public String getTutorPayable() {
		return tutorPayable;
	}

	public void setTutorPayable(String tutorPayable) {
		this.tutorPayable = tutorPayable;
	}

	public String getTutorPaid() {
		return tutorPaid;
	}

	public void setTutorPaid(String tutorPaid) {
		this.tutorPaid = tutorPaid;
	}

	public String getFirstBookPayable() {
		return firstBookPayable;
	}

	public void setFirstBookPayable(String firstBookPayable) {
		this.firstBookPayable = firstBookPayable;
	}

	public String getFirstBookPaid() {
		return firstBookPaid;
	}

	public void setFirstBookPaid(String firstBookPaid) {
		this.firstBookPaid = firstBookPaid;
	}

	public String getFirstPayable() {
		return firstPayable;
	}

	public void setFirstPayable(String firstPayable) {
		this.firstPayable = firstPayable;
	}

	public String getFirstPaid() {
		return firstPaid;
	}

	public void setFirstPaid(String firstPaid) {
		this.firstPaid = firstPaid;
	}

	public String getFirstOffer() {
		return firstOffer;
	}

	public void setFirstOffer(String firstOffer) {
		this.firstOffer = firstOffer;
	}

	public String getFirstIsPayUp() {
		return firstIsPayUp;
	}

	public void setFirstIsPayUp(String firstIsPayUp) {
		this.firstIsPayUp = firstIsPayUp;
	}

	public String getSecondBookPayable() {
		return secondBookPayable;
	}

	public void setSecondBookPayable(String secondBookPayable) {
		this.secondBookPayable = secondBookPayable;
	}

	public String getSecondBookPaid() {
		return secondBookPaid;
	}

	public void setSecondBookPaid(String secondBookPaid) {
		this.secondBookPaid = secondBookPaid;
	}

	public String getSecondPayable() {
		return secondPayable;
	}

	public void setSecondPayable(String secondPayable) {
		this.secondPayable = secondPayable;
	}

	public String getSecondPaid() {
		return secondPaid;
	}

	public void setSecondPaid(String secondPaid) {
		this.secondPaid = secondPaid;
	}

	public String getSecondOffer() {
		return secondOffer;
	}

	public void setSecondOffer(String secondOffer) {
		this.secondOffer = secondOffer;
	}

	public String getSecondIsPayUp() {
		return secondIsPayUp;
	}

	public void setSecondIsPayUp(String secondIsPayUp) {
		this.secondIsPayUp = secondIsPayUp;
	}

	public String getThirdBookPayable() {
		return thirdBookPayable;
	}

	public void setThirdBookPayable(String thirdBookPayable) {
		this.thirdBookPayable = thirdBookPayable;
	}

	public String getThirdBookPaid() {
		return thirdBookPaid;
	}

	public void setThirdBookPaid(String thirdBookPaid) {
		this.thirdBookPaid = thirdBookPaid;
	}

	public String getThirdPayable() {
		return thirdPayable;
	}

	public void setThirdPayable(String thirdPayable) {
		this.thirdPayable = thirdPayable;
	}

	public String getThirdPaid() {
		return thirdPaid;
	}

	public void setThirdPaid(String thirdPaid) {
		this.thirdPaid = thirdPaid;
	}

	public String getThirdOffer() {
		return thirdOffer;
	}

	public void setThirdOffer(String thirdOffer) {
		this.thirdOffer = thirdOffer;
	}

	public String getThirdIsPayUp() {
		return thirdIsPayUp;
	}

	public void setThirdIsPayUp(String thirdIsPayUp) {
		this.thirdIsPayUp = thirdIsPayUp;
	}

	public String getFirstNetPayable() {
		return firstNetPayable;
	}

	public void setFirstNetPayable(String firstNetPayable) {
		this.firstNetPayable = firstNetPayable;
	}

	public String getFirstNetPaid() {
		return firstNetPaid;
	}

	public void setFirstNetPaid(String firstNetPaid) {
		this.firstNetPaid = firstNetPaid;
	}

	public String getSecondNetPayable() {
		return secondNetPayable;
	}

	public void setSecondNetPayable(String secondNetPayable) {
		this.secondNetPayable = secondNetPayable;
	}

	public String getSecondNetPaid() {
		return secondNetPaid;
	}

	public void setSecondNetPaid(String secondNetPaid) {
		this.secondNetPaid = secondNetPaid;
	}

	public String getOtherPayable() {
		return otherPayable;
	}

	public void setOtherPayable(String otherPayable) {
		this.otherPayable = otherPayable;
	}

	public String getOtherPaid() {
		return otherPaid;
	}

	public void setOtherPaid(String otherPaid) {
		this.otherPaid = otherPaid;
	}

}
