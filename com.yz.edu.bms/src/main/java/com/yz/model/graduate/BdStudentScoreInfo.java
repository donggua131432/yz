package com.yz.model.graduate;

import java.io.Serializable;
/**
 * 成绩
 * @author lx
 * @date 2017年7月14日 上午9:55:50
 */
import java.util.List;
public class BdStudentScoreInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5228812613841378607L;
	
	private List<ScoreBaseInfo> firstSemester;     //第一学期
	private List<ScoreBaseInfo> secondSemester;    //第二学期
	private List<ScoreBaseInfo> thirdSemester;     //第三学期
	private List<ScoreBaseInfo> fourthSemester;    //第四学期  
	private List<ScoreBaseInfo> fifthSemester;     //第五学期 
	private List<ScoreBaseInfo> sixthSemester;     //第六学期 
	
	public List<ScoreBaseInfo> getFirstSemester() {
		return firstSemester;
	}
	public void setFirstSemester(List<ScoreBaseInfo> firstSemester) {
		this.firstSemester = firstSemester;
	}
	public List<ScoreBaseInfo> getSecondSemester() {
		return secondSemester;
	}
	public void setSecondSemester(List<ScoreBaseInfo> secondSemester) {
		this.secondSemester = secondSemester;
	}
	public List<ScoreBaseInfo> getThirdSemester() {
		return thirdSemester;
	}
	public void setThirdSemester(List<ScoreBaseInfo> thirdSemester) {
		this.thirdSemester = thirdSemester;
	}
	public List<ScoreBaseInfo> getFourthSemester() {
		return fourthSemester;
	}
	public void setFourthSemester(List<ScoreBaseInfo> fourthSemester) {
		this.fourthSemester = fourthSemester;
	}
	public List<ScoreBaseInfo> getFifthSemester() {
		return fifthSemester;
	}
	public void setFifthSemester(List<ScoreBaseInfo> fifthSemester) {
		this.fifthSemester = fifthSemester;
	}
	public List<ScoreBaseInfo> getSixthSemester() {
		return sixthSemester;
	}
	public void setSixthSemester(List<ScoreBaseInfo> sixthSemester) {
		this.sixthSemester = sixthSemester;
	}
}
