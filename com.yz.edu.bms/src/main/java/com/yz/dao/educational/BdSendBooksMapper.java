package com.yz.dao.educational;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.educational.BdSendBookExport;
import com.yz.model.educational.BdSendBooksInfo;
import com.yz.model.educational.BdSendBooksQuery;
import com.yz.model.finance.receipt.BdSfPrint;
import com.yz.model.sf.SFExpressRequest;

/**
 * 外包发书
 * @author lx
 * @date 2017年9月19日 下午2:37:51
 */
public interface BdSendBooksMapper
{

	public List<BdSendBooksInfo> getBdSendBooksInfo(@Param("bookQuery") BdSendBooksQuery bookQuery,@Param("user") BaseUser user);
	
	public SFExpressRequest selectSfInfoBySendId(String sendId);
	
	public int updateStudentSendRemark(@Param("sendId") String sendId, @Param("errMsg") String errMsg);
	
	public void updateBdSendBooks(BdSendBooksInfo sendBooks);
	
	List<BdSfPrint> selectSfPrints(@Param("sendIds") String[] sendIds);
	
	BdSfPrint selectSfPrint(String sendId);

	public List<BdSendBookExport> selectSendExportInfo(@Param("bookQuery")BdSendBooksQuery bookQuery,@Param("user") BaseUser user);
	
	public String getTextBookNameBySendId(@Param("sendId") String sendId);
	
	public void mergeDisposeBdSendBooks(BdSendBooksInfo sendBooks);
}
