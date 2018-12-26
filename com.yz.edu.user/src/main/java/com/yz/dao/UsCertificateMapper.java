package com.yz.dao;

import org.apache.ibatis.annotations.Param;

import com.yz.model.UsCertificate;

public interface UsCertificateMapper {

    int insertSelective(UsCertificate record);

    UsCertificate selectByPrimaryKey(String certId);

    int updateByPrimaryKeySelective(UsCertificate record);

	int count(UsCertificate cert);

	UsCertificate getCertBy(@Param("userId") String userId, @Param("certType") String certType);
	/**
	 * 查询证件绑定关系
	 * @param userId
	 * @param certType
	 * @return
	 */
	int countBy(@Param("userId") String userId, @Param("certType") String certType);

}