package com.yz.service.oa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.oa.RecruiterMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.OaEmployeeAnnex;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.oa.OaEmployeeJobInfo;
import com.yz.model.oa.OaEmployeeOtherInfo;
import com.yz.model.oa.RecruiterInfo;
import com.yz.model.system.SysDict;
import com.yz.service.system.SysDictService;
import com.yz.util.StringUtil;

/**
 * 招生老师管理
 * @author lx
 * @date 2017年7月3日 下午12:07:31
 */
@Service
@Transactional
public class RecruiterService {
	
	private static final Logger log = LoggerFactory.getLogger(RecruiterService.class);
	
	@Autowired
	private YzSysConfig yzSysConfig ; 
	
	@Autowired
	private SysDictService sysDictService ; 
	
	@Autowired
	private RecruiterMapper recruiterMapper;
	
	@Autowired
	private OaEmployeeMapper employeeMapper;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryRecruiterInfoByPage(int start, int length,RecruiterInfo recruiterInfo) {
		PageHelper.offsetPage(start, length);
		List<RecruiterInfo> recruiterInfos = recruiterMapper.selectAllRecruiterInfo(recruiterInfo);
		if(null != recruiterInfos && recruiterInfos.size()>0){
			for(RecruiterInfo info :recruiterInfos){
				List<Map<String, String>> jdIdMap =findEmpTitle(info.getEmpId());
				
				if(null != jdIdMap && jdIdMap.size()>0){
					String[] jdIds = new String[jdIdMap.size()];
					for(int i=0;i<jdIdMap.size();i++){
						jdIds[i]=jdIdMap.get(i).get("jtId");
					}
					info.setJdIds(jdIds);
				}
			}
		}
		return new IPageInfo((Page) recruiterInfos);
	}

	public Object insertEmpBaseInfo(OaEmployeeBaseInfo baseInfo,OaEmployeeOtherInfo otherInfo){
		baseInfo.setEmpId(IDGenerator.generatorId());
		employeeMapper.insertEmpBaseInfo(baseInfo); //
		initEmployeeAnnex(baseInfo);
		
		otherInfo.setEmpId(baseInfo.getEmpId());
		initEmpOther(otherInfo);
		OaEmployeeJobInfo jobInfo = new OaEmployeeJobInfo();
		jobInfo.setEmpId(baseInfo.getEmpId());
		initEmpJob(jobInfo);
		
		return baseInfo.getEmpId();
	}
	
	
	public void updateEmpBaseInfo(OaEmployeeBaseInfo baseInfo,OaEmployeeOtherInfo otherInfo){
		employeeMapper.updateEmpBaseInfo(baseInfo);
		updateEmpOther(otherInfo);
	}
	/**
	 * 初始化附件信息
	 * @param baseInfo
	 */
	public void initEmployeeAnnex(OaEmployeeBaseInfo baseInfo) {
		// 初始化附件信息
		List<SysDict> annexTypes = sysDictService.getDicts("empAnnexType");
		if (annexTypes != null && !annexTypes.isEmpty()) {
			List<OaEmployeeAnnex> annexInfos = new ArrayList<OaEmployeeAnnex>();
			for (SysDict dict : annexTypes) {
				OaEmployeeAnnex annexInfo = new OaEmployeeAnnex();
				annexInfo.setEmpId(baseInfo.getEmpId());
				annexInfo.setIsRequire(dict.getExt1());
				annexInfo.setEmpAnnexType(dict.getDictValue());
				annexInfo.setAnnexName(dict.getDictName());
				annexInfo.setAnnexId(IDGenerator.generatorId());
				annexInfos.add(annexInfo);
			}
			employeeMapper.insertEmpAnnexInfos(annexInfos);
			log.debug("---------------------------- 招生老师[" + baseInfo.getEmpId() + "]附件信息初始化成功");
		}
	}
	
	public void initEmpOther(OaEmployeeOtherInfo other) {
		boolean isDeleteFile = false;
		String realFilePath = null;
		//处理头像
		Object headPic = other.getHeadPic();
		String headPortrait = other.getHeadPortrait();
		
		byte[] fileByteArray = null;
		boolean isUpdate = false;

		if (headPic != null && headPic instanceof MultipartFile) {
			MultipartFile file = (MultipartFile) other.getHeadPic();

			if (StringUtil.isEmpty(headPortrait)) {
				realFilePath = FileSrcUtil.createFileSrc(Type.EMPLOYEE, other.getEmpId(), file.getOriginalFilename());
			} else {
				realFilePath = headPortrait;
			}
			
			try {
				fileByteArray = file.getBytes();
				isUpdate = true;
			} catch (IOException e) {
				log.error("文件上传失败", e);
			}

			other.setHeadPortrait(realFilePath);
		} else {
			realFilePath = headPortrait;
			other.setHeadPortrait("");
			isDeleteFile = true;
		}
		
		OaEmployeeAnnex annexInfo = new OaEmployeeAnnex();
		annexInfo.setEmpAnnexType(StudentConstants.RECRUITER_ANNEX_TYPE_PHOTO);
		annexInfo.setEmpId(other.getEmpId());
		annexInfo.setAnnexUrl(other.getHeadPortrait());
		
		employeeMapper.updateAnnexInfo(annexInfo);
		// 新增附属信息
		if(StringUtil.isEmpty(other.getFinishTime())){
			other.setFinishTime(null);
		}
		employeeMapper.insertEmpOtherInfo(other);
		
		String bucket = yzSysConfig.getBucket();
		if (isDeleteFile) {
			FileUploadUtil.delete(bucket, realFilePath);
		} else if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}
		
		log.debug("---------------------------- 招生老师[" + other.getEmpId() + "]附属信息初始化成功");
	}
	
	public void updateEmpOther(OaEmployeeOtherInfo other) {
		boolean isDeleteFile = false;
		String realFilePath = null;
		boolean isUpdate = false;
		byte[] fileByteArray = null;
		if ("1".equals(other.getIsPhotoChange())) {
			Object headPic = other.getHeadPic();
			String headPortrait = other.getHeadPortrait();

			if (headPic != null && headPic instanceof MultipartFile) {
				MultipartFile file = (MultipartFile) other.getHeadPic();

				if (StringUtil.isEmpty(headPortrait)) {
					realFilePath = FileSrcUtil.createFileSrc(Type.EMPLOYEE, other.getEmpId(), file.getOriginalFilename());
				} else {
					realFilePath = headPortrait;
				}
				try {
					fileByteArray = file.getBytes();
					isUpdate = true;
				} catch (IOException e) {
					log.error("文件上传失败", e);
				}
				other.setHeadPortrait(realFilePath);
			} else {
				realFilePath = headPortrait;
				other.setHeadPortrait("");
				isDeleteFile = true;
			}
		}
		OaEmployeeAnnex annexInfo = new OaEmployeeAnnex();
		annexInfo.setEmpAnnexType(StudentConstants.RECRUITER_ANNEX_TYPE_PHOTO);
		annexInfo.setEmpId(other.getEmpId());
		annexInfo.setAnnexUrl(other.getHeadPortrait());
		
		employeeMapper.updateAnnexInfo(annexInfo);
		if(StringUtil.isEmpty(other.getFinishTime())){
			other.setFinishTime(null);
		}
		employeeMapper.updateOtherInfo(other);// 更新附属信息

		String bucket = yzSysConfig.getBucket();
		if (isDeleteFile) {
			FileUploadUtil.delete(bucket, realFilePath);
		} else if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}

		log.debug("---------------------------- 招生老师[" + other.getEmpId() + "]附属信息更新成功");
	}
	
	public void initEmpJob(OaEmployeeJobInfo jobInfo){
		
		// 初始化部门信息
		if(StringUtil.isEmpty(jobInfo.getEntryDate())){
			jobInfo.setEntryDate(null);
		}
		if(StringUtil.isEmpty(jobInfo.getLeaveDate())){
			jobInfo.setLeaveDate(null);
		}
		if(StringUtil.isEmpty(jobInfo.getPactStartTime())){
			jobInfo.setPactStartTime(null);
		}
		if(StringUtil.isEmpty(jobInfo.getPactEndTime())){
			jobInfo.setPactEndTime(null);
		}
		employeeMapper.initEmpJob(jobInfo);
		
		log.debug("---------------------------- 招生老师[" + jobInfo.getEmpId() + "]部门信息初始化成功");
	}
	
	public void deleteRecruiter(String[] idArray) {
		employeeMapper.deleteRecruiter(idArray);
	}
	
	public List<Map<String, String>> findEmpTitle(String empId){
		return employeeMapper.findEmpTitle(empId);
	}
	/**
	 * 远智编码是否存在
	 * @param yzCode
	 * @return
	 */
	public int isYzCodeExist(String yzCode){
		return employeeMapper.isYzCodeExist(yzCode);
	}
	/**
	 * 某个员工的某个远智编码是否存在
	 * @param yzCode
	 * @param empId
	 * @return
	 */
	public int isSelfYzCode(String yzCode,String empId){
		return employeeMapper.isSelfYzCode(yzCode,empId);
	}
	/**
	 * 招生编码是否存在
	 * @param recruitCode
	 * @return
	 */
	public int isRecruitCodeExist(String recruitCode){
		return employeeMapper.isRecruitCodeExist(recruitCode);
	}
	/**
	 * 某个员工的招生编码是否存在
	 * @param recruitCode
	 * @param empId
	 * @return
	 */
	public int isSelfRecruiteCode(String recruitCode,String empId){
		return employeeMapper.isSelfRecruiteCode(recruitCode,empId);
	}
	
	/**
	 * 根据yzCode查询用户是否存在
	 * @param yzCode
	 * @return
	 */
	public int checkUserIfExistsByYzCode(String yzCode){
		return employeeMapper.checkUserIfExistsByYzCode(yzCode);
	}
}
