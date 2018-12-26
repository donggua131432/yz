package com.yz.dao.refund;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.refund.BdCheckWeight;
import com.yz.model.refund.BdRefundQuery;
import com.yz.model.refund.BdRefundReponse;
import com.yz.model.refund.BdStudentRefund;
import com.yz.model.transfer.BdCheckRecord;

public interface StdRefundMapper {

	List<Map<String, String>> findStudentInfo(@Param("stdName") String stdName, @Param("phone") String phone,
			@Param("idCard") String idCard, @Param("user") BaseUser user);

	List<BdCheckWeight> getCheckWeight(String stdStage);

	Map<String, String> findStudentInfoById(String learnId);

	Map<String, String> findStudentInfoByGraStdId(String stdId, String grade);

	List<Map<String, String>> selectOpareRecord(String outId, String order);

	String getCheckOrder(@Param("checkType") String checkType, @Param("jtId") String jtId);

	int insertSelective(BdStudentRefund refund);

	List<BdRefundReponse> selectRefundByPage(@Param("query") BdRefundQuery query, @Param("user") BaseUser user);

	BdRefundReponse selectRefundInfo(String refundId);

	List<BdRefundReponse> selectDirectorApproval(@Param("query") BdRefundQuery query, @Param("user") BaseUser user);

	int deleteRefunds(@Param("refundIds") String[] refundIds);

	String[] selectUnCheckRefund(@Param("refundIds") String[] refundIds);

	int selectByLearnId(String learnId);

	String selectStdId(String learnId);

	int updateRefundCheckOrder(BdStudentRefund refund);

	int updateReject(BdCheckRecord bcr);

	List<BdRefundReponse> selectFinancialApproval(BdRefundQuery query);

	List<BdRefundReponse> selectPrincipalApproval(@Param("query") BdRefundQuery query, @Param("user") BaseUser user);

	int finishRefund(BdStudentRefund refund);

}