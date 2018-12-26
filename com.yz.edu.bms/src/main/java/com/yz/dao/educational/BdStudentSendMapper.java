package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.baseinfo.BdUnvsProfession;
import com.yz.model.educational.BdStudentSend;
import com.yz.model.educational.BdStudentSendImport;
import com.yz.model.educational.BdStudentSendMap;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.statistics.PfsnMatchBookStatInfo;

public interface BdStudentSendMapper {
	int deleteByPrimaryKey(String sendId);

	int insert(BdStudentSend record);

	int insertSelective(BdStudentSend record);

	BdStudentSend selectByPrimaryKey(String sendId);

	int updateByPrimaryKeySelective(BdStudentSend record);

	int updateByPrimaryKey(BdStudentSend record);

	List<Map<String, Object>> findAllStudentSendSevi(BdStudentSendMap studentSendMap);

	void rejectedStudentSend(@Param("ids") String[] ids,@Param("reason") String reason,@Param("type") String type,@Param("user") BaseUser user);

	void passStudentSend(@Param("ids") String[] ids);

	List<Map<String, Object>> findAllOkSend(BdStudentSendMap studentSendMap);

	void okSend(@Param("ids") String[] ids);

	List<Map<String, Object>> findAllStudentSendSeviEd(BdStudentSendMap studentSendMap);

	List<Map<String, Object>> findAllStudentOrderBook(BdStudentSendMap studentSendMap);

	Map<String, Object> findCourseFD(String sendID);

	Map<String, Object> findCourseNoFD(String sendID);

	void okOrder(@Param("ids") String[] ids,@Param("logisticsName") String logisticsName);

	void noOrder(@Param("ids") String[] ids);

	void okReceive(@Param("ids") String[] ids);

	void passEducation(@Param("ids") String[] ids);

	void rejectedEducation(@Param("ids") String[] ids,@Param("reason") String reason,@Param("type") String type,@Param("user") BaseUser user);

	BdStudentSend getRecordByLearnId(@Param("learnId") String learnId,@Param("semester") String semester);

	void insertBdTextBookSend(@Param("sendId") String sendId, @Param("learnId") String learnId,
			@Param("semester") String semester);

	String[] selectTestSubByLearnId(String learnId);

	void insertBdTextBookSendFD(@Param("sendId") String sendId, @Param("testSubject") String[] testSubject);

	void updateSendAddressStatus(BdStudentBaseInfo baseInfo);

	int selectTestBookCount(@Param("testSubject") String[] testSubject);

	String selectLearnId(@Param("idCard") String idCard, @Param("grade") String grade,
			@Param("pfsnName") String pfsnName, @Param("unvsName") String unvsName);

	public void queryOkOrder(BdStudentSendMap studentSendMap);

	public List<PfsnMatchBookStatInfo> exportMatchBookInfo(BdStudentSendMap studentSendMap);

	String selectFdSendId(String learnId);

	int deleteTextBookSend(String sendId);

	int deleteBookSend(String sendId);

	public List<BdStudentSend> selectRefreshTextbookData(@Param("ids") String[] ids);

	int selectSendBookNum(String sendId);

	int selectBookSendCount(@Param("semester") String semester, @Param("learnId") String learnId);

	List<String> selectSendId(@Param("semester") String semester, @Param("learnId") String learnId);

	int selectFdSendCount(String learnId);

	List<String> selectFdSendIds(String learnId);
	
	
	List<Map<String, Object>> getNonExistsStudent(@Param("stuOrderBookList") List<BdStudentSendImport> stuOrderBookList);
	
	public void updateStuSendByCond(BdStudentSendImport sendImport);
	
	public String getTeacherUserIdByLearnId(String learnId);
	
	public String getStuNameByLearnId (String learnId);
	
	public void resetAddressTask(String learnId);
	
	public void addRemark(@Param("sendId") String sendId,@Param("remark") String remark);
	
	public void updateAddress(@Param("sendId") String sendId,@Param("provinceCode") String provinceCode,@Param("cityCode") String cityCode,@Param("districtCode") String districtCode,@Param("detailAddress") String detailAddress,
			@Param("takeUserName") String takeUserName,@Param("takeMobile") String takeMobile);
	
	/**
	 * 初始化临时表
	 * @param list
	 */
	void initTmpUnvsPsfnInfoTable(@Param("list") List<BdUnvsProfession> list);
	/**
	 * 根据初始化的临时表得到不存在院校信息
	 * @param list
	 * @return
	 */
	List<Map<String, Object>> getNonExistsUnvsNamePsfnName();
	
	/**
	 * 批量插入excel
	 * @param list
	 */
	void insertByExcel();
}