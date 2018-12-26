package com.yz.model.graduate;

import java.io.Serializable;
import java.util.List;

import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
/**
 * 学费缴费审核
 * @author lx
 * @date 2017年7月14日 下午3:14:09
 */
public class FeeCheckDataInfo extends CheckDataBaseInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2681073870089355031L;
	
	private List<BdStdPayInfoResponse> payInfos;

	public List<BdStdPayInfoResponse> getPayInfos() {
		return payInfos;
	}

	public void setPayInfos(List<BdStdPayInfoResponse> payInfos) {
		this.payInfos = payInfos;
	}
}
