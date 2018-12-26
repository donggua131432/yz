package com.yz.dao.finance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.receipt.BdPayItem;
import com.yz.model.finance.receipt.BdReceipt;
import com.yz.model.finance.receipt.BdReceiptPrint;
import com.yz.model.finance.receipt.BdReceiptQuery;
import com.yz.model.finance.receipt.BdSfPrint;
import com.yz.model.sf.SFExpressRequest;

public interface BdReptMapper {

	String selectLearnIdByReptId(String reptId);

	String selectContactMobile(String empId);

	int insertSelective(BdReceipt rept);

	int updateRept(BdReceipt rept);

	BdSfPrint selectSfPrint(String reptId);

	List<BdReceipt> selectReptByPage(BdReceiptQuery query);

	int updateReptRemark(@Param("reptId") String reptId, @Param("errMsg") String errMsg);

	List<BdReceiptPrint> selectReceiptPrints(@Param("reptIds") String[] reptIds);

	List<BdSfPrint> selectSfPrints(@Param("reptIds") String[] reptIds);

	int updateReptMailed(@Param("reptIds") String[] reptIds);

	SFExpressRequest selectSfInfoByLearnId(@Param("reptId") String reptId);

	int insertReptSerial(@Param("rsId") String rsId, @Param("reptId") String reptId,
			@Param("serialMark") String serialMark);

	String selectReptType(String reptId);

	String selectUserIdByReptId(String reptId);

	String selectMailNoByReptId(String reptId);

	String selectHasInformByReptId(String reptId);

	int updateSendInform(String reptId);

	String selectApplyTimeByReptId(String reptId);

	String selectReptCampusAddress(String reptId);

	Map<String, String> selectStdInfoBySerialMark(String serialMark);

	int selectReptCountBySerialMark(String serialMark);

	String selectReptIdBySerialMark(String serialMark);

	List<BdPayItem> selectItemsByReptId(String reptId);

	String selectReptAmount(BdReceiptQuery query);

	BdReceipt selectReptById(String reptId);

	void updateReptStatus(@Param("reptId") String reptId, @Param("status") String status);

}
