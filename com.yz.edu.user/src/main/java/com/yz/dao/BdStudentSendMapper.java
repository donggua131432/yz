package com.yz.dao;

import org.apache.ibatis.annotations.Param;

import com.yz.model.educational.BdStudentSend;

public interface BdStudentSendMapper {

	int insertSelective(BdStudentSend record);

	void insertBdTextBookSend(@Param("sendId") String sendId, @Param("learnId") String learnId);

	String[] selectTestSubByLearnId(String learnId);

	void insertBdTextBookSendFD(@Param("sendId") String sendId, @Param("testSubject") String[] testSubject);

	void updateBookReceiveAddressAndStatus(@Param("learnId") String learnId, @Param("saName") String saName,
			@Param("mobile") String mobile, @Param("address") String address,
			@Param("provinceCode") String provinceCode, @Param("cityCode") String cityCode,
			@Param("districtCode") String districtCode);

	int selectTestBookCount(@Param("testSubject") String[] testSubject);

}