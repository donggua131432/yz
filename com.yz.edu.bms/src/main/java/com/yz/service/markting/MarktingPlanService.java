package com.yz.service.markting;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.markting.BdMarktingJarMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.SystemException;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.common.PageCondition;
import com.yz.model.markting.BdMarktingJar;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;

@Service
public class MarktingPlanService {
	
	@Autowired
	private YzSysConfig yzSysConfig ;
	
	@Autowired
	private BdMarktingJarMapper jarMapper;
	
	private static final Logger log = LoggerFactory.getLogger(MarktingPlanService.class);

	public IPageInfo<Map<String, String>> getMpJarList(PageCondition pc) {
		PageHelper.offsetPage(pc.getStart(), pc.getLength());
		List<Map<String, String>> jarList = jarMapper.getList();
		
		if(jarList == null)
			return null;
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) jarList);
	}
	
	@Transactional
	public void update(BdMarktingJar jarInfo) {
		jarMapper.updateByPrimaryKeySelective(jarInfo);
	}

	@Transactional
	public void add(BdMarktingJar jarInfo) {
		String realFilePath = null;
		byte[] fileByteArray = null;

		Object jar = jarInfo.getJar();

		if (jar != null && jar instanceof MultipartFile) {
			MultipartFile file = (MultipartFile) jar;
			realFilePath = FileSrcUtil.createOrign(Type.MPJAR, file.getOriginalFilename());
			try {
				fileByteArray = file.getBytes();
			} catch (IOException e) {
				throw new SystemException(e.getMessage(), e);
			}
			
			jarInfo.setJarUrl(realFilePath);
		} else {
			throw new IllegalArgumentException("请上传jar包！");
		}
		
		jarInfo.setUploadUser(jarInfo.getCreateUser());
		jarInfo.setUploadUserId(jarInfo.getCreateUserId());
		jarInfo.setFlowId(IDGenerator.generatorId());
		jarMapper.insertSelective(jarInfo);

		String bucket = yzSysConfig.getBucket();

		FileUploadUtil.upload(bucket, realFilePath, fileByteArray);

		log.debug("---------------------------- 学员[" + jarInfo.getJarName() + "]附属信息更新成功");
	}

	@Transactional
	public void delete(String jarName) {
		BdMarktingJar jarInfo = jarMapper.selectByPrimaryKey(jarName);
		
		if(GlobalConstants.TRUE.equals(jarInfo.getIsAllow())) {
			throw new IllegalArgumentException("jar启用中无法删除");
		}
		
		jarMapper.deleteByPrimaryKey(jarInfo.getJarName());
	}

	@Transactional
	public void start(String jarName) {
		jarMapper.clearStatus();
		BdMarktingJar jarInfo = jarMapper.selectByPrimaryKey(jarName);
		jarInfo.setIsAllow(GlobalConstants.TRUE);
		jarMapper.updateByPrimaryKeySelective(jarInfo);
		
		initJarInfo();
		
		//SimpleCacheUtil cacheUtil = RemoteCacheUtilFactory.simple();
		
		//cacheUtil.publish(GlobalConstants.BDS_CRF_CHANNEL, "MPJAR");
		//cacheUtil.publish(GlobalConstants.US_CRF_CHANNEL, "MPJAR");
		RedisService.getRedisService().publish(GlobalConstants.BDS_CRF_CHANNEL, "MPJAR");
		RedisService.getRedisService().publish(GlobalConstants.US_CRF_CHANNEL, "MPJAR");
	}
	
	/**
	 * 
	 * @param jarName
	 * @return
	 */
	public BdMarktingJar getJarInfo(String jarName) {
		return jarMapper.selectByPrimaryKey(jarName);
	}

	public boolean isExsit(String jarName) {
		int count = jarMapper.countBy(jarName);
		
		if(count > 1)
			return false;
		
		return true;
	}
	
	public BdMarktingJar getAllowJar() {
		return jarMapper.selectAllowJar();
	}
	
//	@Autowired
//	private MPClassLoader mpClassLoader;
	
	@Value("${cacheFile.mpJar}")
	private String mpJarFileName;

	public void initJarInfo() {
		BdMarktingJar jarInfo = getAllowJar();
		
		if(jarInfo != null) {
			String url = jarInfo.getJarUrl();
			String stageFlowClass = jarInfo.getStageClass();
			String chargeFlowClass = jarInfo.getChargeClass();
			String registerFlowClass = jarInfo.getRegisterClass();
			String iChargeFlowClass = jarInfo.getiChargeClass();
			
			String flowId = jarInfo.getFlowId();
			
			String bucket = yzSysConfig.getBucket();
			
			String fileSrc = FileSrcUtil.createOrign(Type.MPJAR, mpJarFileName);
			
			String json = JsonUtil.object2String(jarInfo);
			
			try {
				FileUploadUtil.upload(bucket, fileSrc, json.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
//			String fileSuffix = yzSysConfig.getFileBrowserUrl();
	
//			mpClassLoader.init(fileSuffix + url , stageFlowClass, chargeFlowClass, registerFlowClass, iChargeFlowClass, flowId);
		}
	}

}
