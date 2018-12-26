package com.yz.dao.refund;

import java.util.List;
import java.util.Map;

import com.yz.model.refund.BdStudentWithdraw;
import com.yz.model.refund.BdWithdrawQuery;
import com.yz.model.refund.BdWithdrawResponse;

public interface BdStudentWithdrawMapper {
    int deleteByPrimaryKey(String withdrawId);

    int insert(BdStudentWithdraw record);

    int insertSelective(BdStudentWithdraw record);

    BdStudentWithdraw selectByPrimaryKey(String withdrawId);

    int updateByPrimaryKeySelective(BdStudentWithdraw record);

    int updateByPrimaryKey(BdStudentWithdraw record);

	List<BdWithdrawResponse> selectWithdrawByPage(BdWithdrawQuery query);

	String selectUserIdByStdId(String stdId);

	BdWithdrawResponse selectWithdrawById(String withdrawId);

	String selectUserIdByWithId(String withdrawId);
	
	public List<BdWithdrawResponse> selectWithdrawByExport(BdWithdrawQuery query);
	
	List<Map<String, String>> selectUnvsInfoByStdId(String stdId);
}