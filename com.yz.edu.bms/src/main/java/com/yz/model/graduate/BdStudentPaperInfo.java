package com.yz.model.graduate;

import java.io.Serializable;
/**
 * 毕业论文信息
 * @author lx
 * @date 2017年7月14日 下午2:25:40
 */
public class BdStudentPaperInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4909395799696971963L;
	
	private String paperScore; //论文成绩
	private String isPass;     //是否通过
	public String getPaperScore() {
		return paperScore;
	}
	public void setPaperScore(String paperScore) {
		this.paperScore = paperScore;
	}
	public String getIsPass() {
		return isPass;
	}
	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}

}
