package com.yz.dao.stdService;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentCertificateMapper {

    List<StudentCertificateInfo> findAllList(@Param("queryInfo") StudentCertificateQuery query, @Param("user") BaseUser user);

	StudentCertificateInfo getCertificate(@Param("certId") String certId);

	/**
	 * 更新快递单号
	 * @param certId
	 * @param expressNo
	 * @return
	 */
	int updateExpressNo(@Param("certId") String certId, @Param("expressNo") String expressNo);

	int updateCheckOrder(@Param("certId") String certId, @Param("checkOrder") String checkOrder);

	List<Map<String, Object>> getNonExistsStudent(@Param("studentCertificateExcelList") List<StudentCertificateExcel> studentCertificateExcelList);

	void updateExpressNoByExcel(@Param("studentCertificateExcelList") List<StudentCertificateExcel> studentCertificateExcelList, @Param("user") BaseUser user);

	Map<String,Object> getStudyInfo(@Param("learnId") String learnId);
}
