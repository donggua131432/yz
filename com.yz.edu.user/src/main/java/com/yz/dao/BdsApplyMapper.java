package com.yz.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.model.apply.BdsStudentCertificateInfo;
import org.apache.ibatis.annotations.Param;

import com.yz.model.BdStudentRept;
import com.yz.model.apply.BdStudentWithdraw;
import com.yz.model.apply.BdsApplyInfo;
import com.yz.model.identity.BdRelation;

public interface BdsApplyMapper {

	List<BdsApplyInfo> selectApplyInfoByLearnId(String learnId);

	HashMap<String, String> selectEmpId(@Param("idCard") String idCard, @Param("idType") String idType);

	HashMap<String, String> selectStdId(@Param("idCard") String idCard, @Param("idType") String idType);

	int insertRelation(BdRelation rel);

	BdRelation selectRelationByUserId(String userId);

	int insertWithdraw(BdStudentWithdraw withdraw);

	List<String> selectUnReptSerialInfo(String learnId);

	int insertReptApply(BdStudentRept rept);

	/**
	 * 删除原有关系
	 * 
	 * @param stdId
	 * @return
	 */
	int deleteRelation(String stdId);

	/**
	 * 删除未支付收据申请
	 * 
	 * @param string
	 * @return
	 */
	int deleteReptUnpaid(String string);

	List<Map<String, String>> selectCampusInfo();

	String selectStdByLearnId(String string);

	int insertReptSerialMark(@Param("reptId") String reptId, @Param("serials") List<String> serials);

	/**
	 * 根据收据id查找快递金额
	 * 
	 * @param reptId
	 * @return
	 */
	String selectExpressAmountByReptId(String reptId);

	int updateReptPaySuccess(@Param("reptId") String reptId, @Param("paymentType") String paymentType,
			@Param("outSerialNo") String outSerialNo);

	/**
	 * 查询是否已缴费
	 * @param reptId
	 * @return
	 */
	int selectPaidRept(String reptId);

	void insertStudentCertificate(BdsStudentCertificateInfo studentCertificateInfo);

	List<Map<String, String>> getCertificateApply(@Param("learnId") String learnId);

	String selectReptCampusAddress(String campusId);
}
