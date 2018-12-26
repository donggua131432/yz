package com.yz.service.gk;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileUploadUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.dao.gk.BdStudentGKUnifiedExamMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.gk.BdStudentGkUnifiedExamInfo;
import com.yz.model.gk.BdStudentGkUnifiedExamQuery;
/**
 * 国开统考设置
 * @author lx
 * @date 2018年6月6日 上午11:34:52
 */
@Service
@Transactional
public class BdStudentGKUnifiedExamService {
	
	private static final Logger log = LoggerFactory.getLogger(BdStudentGKUnifiedExamService.class);

	@Autowired
	private BdStudentGKUnifiedExamMapper stdGKUnifiedExamMapper;
	
	@Autowired
	private YzSysConfig yzSysConfig;
	
	/**
	 * 国开统考列表查询
	 * @param query
	 * @return
	 */
	public Object findStdGKUnifiedExamList(BdStudentGkUnifiedExamQuery query){
		List<BdStudentGkUnifiedExamInfo> list = stdGKUnifiedExamMapper.findStdGKUnifiedExamList(query);
		 return new IPageInfo<>((Page<BdStudentGkUnifiedExamInfo>) list);
	}
	/**
	 * 新增统考设置
	 * @param info
	 */
	public void insertStdGKUnifiedExamInfo(BdStudentGkUnifiedExamInfo info){
		info.setId(IDGenerator.generatorId());
		updateGkUnifiedExamAttachment(info);
		stdGKUnifiedExamMapper.insertStdGKUnifiedExamInfo(info);
	}
	/**
	 * 获取某个具体的
	 * @param id
	 * @return
	 */
	public BdStudentGkUnifiedExamInfo getGkUnifiedExam(String id){
		return stdGKUnifiedExamMapper.getGkUnifiedExam(id);
	}
	
	/**
	 * 删除统考设置
	 * @param id
	 */
	public void delGkUnifiedExam(String id){
		
		//验证是否被任务引用
		if(stdGKUnifiedExamMapper.getTaskCountByGKUniFiedId(id)>0){
			throw new BusinessException("E000203");
		}
		//删除
		stdGKUnifiedExamMapper.delGkUnifiedExam(id);
	}
	
	/**
	 * 启用或者禁用
	 * @param id
	 * @param ifShow
	 */
	public void startOrStopGkUnifiedExam(String id,String ifShow){
		stdGKUnifiedExamMapper.startOrStopGkUnifiedExam(id,ifShow);
	}
	
	/**
	 * 修改
	 * @param info
	 */
	public void updateGkUnifiedExam(BdStudentGkUnifiedExamInfo info){
		updateGkUnifiedExamAttachment(info);
		stdGKUnifiedExamMapper.updateGkUnifiedExam(info);
	}
	
	/**
	 * 处理附件
	 * @param examInfo
	 */
	private void updateGkUnifiedExamAttachment(BdStudentGkUnifiedExamInfo examInfo) {
		boolean isDeleteFile = false;
		String realFilePath = null;
		boolean isUpdate = false;
		byte[] fileByteArray = null;
		if ("1".equals(examInfo.getIsPhotoChange())) {
			//处理头像
			Object headPic = examInfo.getAnnexUrl();
			String headPortrait = examInfo.getFileUrl();

			if (headPic != null && headPic instanceof MultipartFile) {
				MultipartFile file = (MultipartFile) headPic;
				realFilePath = FileSrcUtil.createFileSrc(Type.GOODS, examInfo.getId(), file.getOriginalFilename());
				try {
					fileByteArray = file.getBytes();
					isUpdate = true;
				} catch (IOException e) {
					log.error("文件上传失败", e);
				}

				examInfo.setFileUrl(realFilePath);
				examInfo.setFileName(file.getOriginalFilename());
			} else {
				realFilePath = headPortrait;
				examInfo.setFileUrl("");
				examInfo.setFileName("");
				isDeleteFile = true;
				stdGKUnifiedExamMapper.updateGkunifiedFileInfo(examInfo);
			}
		}
		stdGKUnifiedExamMapper.updateGkUnifiedExam(examInfo);
		String bucket = yzSysConfig.getBucket();
		if (isDeleteFile) {
			FileUploadUtil.delete(bucket, realFilePath);
		} else if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}
	}
}
