package com.yz.model.graduate;

import java.io.Serializable;

/**
 * 审核结果 详细
 * @author lx
 * @date 2017年7月14日 下午7:24:41
 */
public class BdCheckResultShowInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 988294952491799616L;

	private BdCheckResultInfo dataResultInfo;
	private BdCheckResultInfo pictureResultInfo;
	private BdCheckResultInfo paperResultInfo;
	private BdCheckResultInfo scoreResultInfo;
	private BdCheckResultInfo feeResultInfo;
	
	public BdCheckResultInfo getDataResultInfo() {
		return dataResultInfo;
	}
	public void setDataResultInfo(BdCheckResultInfo dataResultInfo) {
		this.dataResultInfo = dataResultInfo;
	}
	public BdCheckResultInfo getPictureResultInfo() {
		return pictureResultInfo;
	}
	public void setPictureResultInfo(BdCheckResultInfo pictureResultInfo) {
		this.pictureResultInfo = pictureResultInfo;
	}
	public BdCheckResultInfo getPaperResultInfo() {
		return paperResultInfo;
	}
	public void setPaperResultInfo(BdCheckResultInfo paperResultInfo) {
		this.paperResultInfo = paperResultInfo;
	}
	public BdCheckResultInfo getScoreResultInfo() {
		return scoreResultInfo;
	}
	public void setScoreResultInfo(BdCheckResultInfo scoreResultInfo) {
		this.scoreResultInfo = scoreResultInfo;
	}
	public BdCheckResultInfo getFeeResultInfo() {
		return feeResultInfo;
	}
	public void setFeeResultInfo(BdCheckResultInfo feeResultInfo) {
		this.feeResultInfo = feeResultInfo;
	}
}	
