package com.yz.model;

import java.util.List;

/**
 * 抽奖商品详细
 * @author lx
 * @date 2017年7月25日 下午2:13:13
 */
public class ZmcGoodsLotteryInfo extends GsGoodsBaseInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2135789402974710219L;
	
	private String planId;
	private String planCount;
	private String runCount;
	private String runTime;
    private String winUser;            //中奖人
    private String joinCount;          //已经参加人数

    private List<GsPastLotteryRecord> lotteryRecords;
    private List<GsGoodsComment> comments;
    private List<GsLotteryPart> lotteryParts;
	private List<GsGoodsAnnexInfo> annexInfos;
	private List<GsLotteryPart> winParts; //中奖纪录
	
	private String ifJoin = "N";    //是否已经参加抽奖
    
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}
	public String getRunCount() {
		return runCount;
	}
	public void setRunCount(String runCount) {
		this.runCount = runCount;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getWinUser() {
		return winUser;
	}
	public void setWinUser(String winUser) {
		this.winUser = winUser;
	}
	public String getJoinCount() {
		return joinCount;
	}
	public void setJoinCount(String joinCount) {
		this.joinCount = joinCount;
	}
	public List<GsPastLotteryRecord> getLotteryRecords() {
		return lotteryRecords;
	}
	public void setLotteryRecords(List<GsPastLotteryRecord> lotteryRecords) {
		this.lotteryRecords = lotteryRecords;
	}
	public List<GsGoodsComment> getComments() {
		return comments;
	}
	public void setComments(List<GsGoodsComment> comments) {
		this.comments = comments;
	}
	public List<GsLotteryPart> getLotteryParts() {
		return lotteryParts;
	}
	public void setLotteryParts(List<GsLotteryPart> lotteryParts) {
		this.lotteryParts = lotteryParts;
	}
	public List<GsGoodsAnnexInfo> getAnnexInfos() {
		return annexInfos;
	}
	public void setAnnexInfos(List<GsGoodsAnnexInfo> annexInfos) {
		this.annexInfos = annexInfos;
	}
	public String getIfJoin()
	{
		return ifJoin;
	}
	public void setIfJoin(String ifJoin)
	{
		this.ifJoin = ifJoin;
	}
	public List<GsLotteryPart> getWinParts()
	{
		return winParts;
	}
	public void setWinParts(List<GsLotteryPart> winParts)
	{
		this.winParts = winParts;
	}
}
