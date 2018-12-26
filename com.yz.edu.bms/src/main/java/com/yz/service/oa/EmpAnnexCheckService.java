package com.yz.service.oa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.constants.StudentConstants;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.oa.EmpAnnexCheckMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.OaEmployeeAnnex;
import com.yz.util.Assert;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;
/**
 * 员工附件审查
 * @author lx
 * @date 2017年7月5日 下午4:40:06
 */
@Service
@Transactional
public class EmpAnnexCheckService {

	private static final Logger log = LoggerFactory.getLogger(EmpAnnexCheckService.class);
	
	@Autowired
	private YzSysConfig yzSysConfig ; 

	@Autowired
	private EmpAnnexCheckMapper annexCheckMapper;

	/**
	 * 获取招生老师附件列表
	 * 
	 * @param learnId
	 * @return
	 */
	public IPageInfo<OaEmployeeAnnex> getAnnexList(String empId) {
		List<OaEmployeeAnnex> list = annexCheckMapper.getAnnexList(empId);
		if (list != null && !list.isEmpty()) {
			return new IPageInfo<OaEmployeeAnnex>(list, list.size());
		} else {
			list =new ArrayList<>();
			return new IPageInfo<OaEmployeeAnnex>(list, 0);
		}
	}

	/**
	 * 更新附件信息
	 * 
	 * @param annexInfo
	 */
	public String updateAnnexInfo(OaEmployeeAnnex annexInfo) {
		if (annexInfo.getAnnexFile() == null || !(annexInfo.getAnnexFile() instanceof MultipartFile)) {
			throw new IllegalArgumentException("上传文件不能为空");
		}
		MultipartFile file = (MultipartFile) annexInfo.getAnnexFile();
		Assert.hasText(annexInfo.getAnnexId(), "附件ID不能为空");
		Assert.hasText(annexInfo.getEmpId(), "附件所属招生老师不能为空");
		String annexUrl = annexInfo.getAnnexUrl();
		if (StringUtil.isEmpty(annexUrl)) {
			annexUrl = FileSrcUtil.createFileSrc(Type.EMPLOYEE, annexInfo.getEmpId(), file.getOriginalFilename());
		}

		String bucket = yzSysConfig.getBucket();
		try {
			FileUploadUtil.upload(bucket, annexUrl, file.getBytes());
		} catch (IOException e) {
			log.error("---------------------------- 文件上传失败", e);
		}

		annexInfo.setAnnexUrl(annexUrl);
		annexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_UNCHECK);
		annexInfo.setReason("");
		annexInfo.setUpdateTime(DateUtil.getNowDateAndTime());
		annexCheckMapper.updateAnnexInfo(annexInfo);

		return annexUrl;
	}

	/**
	 * 删除附件信息
	 * 
	 * @param annexInfo
	 */
	public void delAnnexInfo(OaEmployeeAnnex annexInfo) {
		Assert.hasText(annexInfo.getAnnexId(), "附件ID不能为空");
		Assert.hasText(annexInfo.getEmpId(), "附件所属招生老师不能为空");
		Assert.hasText(annexInfo.getAnnexUrl(), "附件地址不能为空");
		String realFilePath = annexInfo.getAnnexUrl();
		
		if(StringUtil.hasValue(realFilePath)) {
			String bucket = yzSysConfig.getBucket();
			FileUploadUtil.delete(bucket, realFilePath);
		}

		annexCheckMapper.updateAnnexInfoForDel(annexInfo);
	}
}
