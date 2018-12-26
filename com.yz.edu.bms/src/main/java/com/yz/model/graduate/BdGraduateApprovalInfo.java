package com.yz.model.graduate;
/**
 * 毕业核准
 * @author lx
 * @date 2017年7月14日 下午5:52:13
 */
public class BdGraduateApprovalInfo extends CheckDataBaseInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2242403139421021193L;
	
	private String graduateId;
	private String status;
	private String dataStatus;
	private String pictureStatus;
	private String paperStatus;
	private String scoreStatus;
	private String feeStatus;
	private String stdStage;
	
	public String getGraduateId() {
		return graduateId;
	}
	public void setGraduateId(String graduateId) {
		this.graduateId = graduateId;
	}
	
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getPictureStatus() {
		return pictureStatus;
	}
	public void setPictureStatus(String pictureStatus) {
		this.pictureStatus = pictureStatus;
	}
	public String getPaperStatus() {
		return paperStatus;
	}
	public void setPaperStatus(String paperStatus) {
		this.paperStatus = paperStatus;
	}
	public String getScoreStatus() {
		return scoreStatus;
	}
	public void setScoreStatus(String scoreStatus) {
		this.scoreStatus = scoreStatus;
	}
	public String getFeeStatus() {
		return feeStatus;
	}
	public void setFeeStatus(String feeStatus) {
		this.feeStatus = feeStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}
}
