package com.yz.service.goods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.core.constants.AppConstants;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.core.util.ImageUtil;
import com.yz.dao.goods.GsGoodsMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.goods.GsActivitiesGoods;
import com.yz.model.goods.GsCourseGoods;
import com.yz.model.goods.GsGoodsAnnex;
import com.yz.model.goods.GsGoodsInsertInfo;
import com.yz.model.goods.GsGoodsQueryInfo;
import com.yz.model.goods.GsGoodsShowInfo;
import com.yz.model.goods.GsGoodsStore;
import com.yz.service.oa.RecruiterService;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
@Transactional
public class GsGoodsService {
	
	private static final Logger log = LoggerFactory.getLogger(RecruiterService.class);
	
	@Autowired
	private YzSysConfig yzSysConfig ;
	
	
	@Autowired
	private GsGoodsMapper gsGoodsMapper;
	
	@Autowired
	private FileUploadUtil fileUploadUtil;

	
	public IPageInfo<GsGoodsShowInfo> queryGsGoodsShowInfoByPage(int start, int length,GsGoodsQueryInfo queryInfo) {
		PageHelper.offsetPage(start, length);
		List<GsGoodsShowInfo> resultList = gsGoodsMapper.getGsGoodsShowInfo(queryInfo);
		return new IPageInfo<GsGoodsShowInfo>((Page<GsGoodsShowInfo>) resultList);
	}
	
	public Object insertGoodsInfo(GsGoodsInsertInfo goodsInfo,String coverUrls){
		goodsInfo.setGoodsId(IDGenerator.generatorId());
		//商品信息
		gsGoodsMapper.addNewGoodsInfo(goodsInfo);
		//库存信息
		GsGoodsStore store = new GsGoodsStore();
		store.setGoodsId(goodsInfo.getGoodsId());
		store.setGoodsCount(goodsInfo.getGoodsCount());
		store.setUpdateUser(goodsInfo.getUpdateUser());
		store.setUpdateUserId(goodsInfo.getUpdateUserId());
		gsGoodsMapper.addNewGsGoodsStore(store);
		
		if(goodsInfo.getGoodsType().equals(AppConstants.GOODS_TYPE_ACTIVITY)){  //现在活动
			GsActivitiesGoods goods = new GsActivitiesGoods();
			goods.setGoodsId(goodsInfo.getGoodsId());
			goods.setAddress(goodsInfo.getAddress());
			goods.setStartTime(goodsInfo.getStartTime());
			goods.setLocation(goodsInfo.getLocation());
			goods.setEndTime(goodsInfo.getEndTime());
			goods.setTakein(goodsInfo.getTakein());
			gsGoodsMapper.addGsActivitiesGoods(goods);
		}
		if(goodsInfo.getGoodsType().equals(AppConstants.GOODS_TYPE_COURSE)){ //课程
			GsCourseGoods goods = new GsCourseGoods();
			goods.setAddress(goodsInfo.getAddress());
			goods.setCourseType(goodsInfo.getCourseType());
			goods.setEndTime(goodsInfo.getEndTime());
			goods.setGoodsId(goodsInfo.getGoodsId());
			goods.setLocation(goodsInfo.getLocation());
			goods.setStartTime(goodsInfo.getStartTime());
			
			gsGoodsMapper.addGsCourseGoods(goods);
		}
		
		//附件
		initGoodsAnnex(goodsInfo);
		//封面
		List<GsGoodsAnnex> annexList = new ArrayList<>();
		JSONArray jsn = JSONArray.fromObject(coverUrls);
		if (null != jsn && jsn.size() > 0) {
			for (int i = 0; i < jsn.size(); i++) {
				GsGoodsAnnex annexInfo = new GsGoodsAnnex();
				//把图片从oss的临时目录copy到正式目录
				JSONObject jo = JSONObject.fromObject(jsn.get(i));
				String id = jo.getString("id");
				String path = jo.getString("path");
				if(id.equals("1")){
					fileUploadUtil.copyToDisplay(path, path);
				}
				annexInfo.setGoodsId(goodsInfo.getGoodsId());
				annexInfo.setAnnexUrl(path);
				annexInfo.setGsAnnexType("2"); // 封面
				annexInfo.setAnnexName(goodsInfo.getGoodsName() + "轮播图");
				annexInfo.setUpdateUser(goodsInfo.getUpdateUser());
				annexInfo.setUpdateUserId(goodsInfo.getUpdateUserId());
				annexInfo.setAnnexId(IDGenerator.generatorId());
				annexList.add(annexInfo);
			}
			//轮播图
			gsGoodsMapper.initGoodsAnnexImgs(annexList);
		}
		return goodsInfo.getGoodsId();
	}
	
	public void updateGoodsInfo(GsGoodsInsertInfo goodsInfo,String coverUrls){
		//商品信息
		gsGoodsMapper.updateGoodsInfo(goodsInfo);
		//库存信息
		GsGoodsStore store = new GsGoodsStore();
		store.setGoodsId(goodsInfo.getGoodsId());
		store.setGoodsCount(goodsInfo.getGoodsCount());
		store.setUpdateUser(goodsInfo.getUpdateUser());
		store.setUpdateUserId(goodsInfo.getUpdateUserId());
		gsGoodsMapper.updateGsGoodsStore(store);
		
		if(goodsInfo.getGoodsType().equals(AppConstants.GOODS_TYPE_ACTIVITY)){  //现在活动
			GsActivitiesGoods goods = new GsActivitiesGoods();
			goods.setGoodsId(goodsInfo.getGoodsId());
			goods.setAddress(goodsInfo.getAddress());
			goods.setStartTime(goodsInfo.getStartTime());
			goods.setLocation(goodsInfo.getLocation());
			goods.setEndTime(goodsInfo.getEndTime());
			goods.setTakein(goodsInfo.getTakein());
			gsGoodsMapper.updateGsActivitiesGoods(goods);
		}
		if(goodsInfo.getGoodsType().equals(AppConstants.GOODS_TYPE_COURSE)){ //课程
			GsCourseGoods goods = new GsCourseGoods();
			goods.setAddress(goodsInfo.getAddress());
			goods.setCourseType(goodsInfo.getCourseType());
			goods.setEndTime(goodsInfo.getEndTime());
			goods.setGoodsId(goodsInfo.getGoodsId());
			goods.setLocation(goodsInfo.getLocation());
			goods.setStartTime(goodsInfo.getStartTime());
			
			gsGoodsMapper.updateGsCourseGoods(goods);
		}
		//附件
		updateGoodsAnnex(goodsInfo);
		//封面
		List<GsGoodsAnnex> annexList = new ArrayList<>();
		JSONArray jsn = JSONArray.fromObject(coverUrls);
		if (null != jsn && jsn.size() > 0) {
			for (int i = 0; i < jsn.size(); i++) {
				GsGoodsAnnex annexInfo = new GsGoodsAnnex();
				//把图片从oss的临时目录copy到正式目录
				JSONObject jo = JSONObject.fromObject(jsn.get(i));
				String id = jo.getString("id");
				String path = jo.getString("path");
				if(id.equals("1")){
					fileUploadUtil.copyToDisplay(path, path);
				}
				annexInfo.setGoodsId(goodsInfo.getGoodsId());
				annexInfo.setAnnexUrl(path);
				annexInfo.setGsAnnexType("2"); // 封面
				annexInfo.setAnnexName(goodsInfo.getGoodsName() + "轮播图");
				annexInfo.setUpdateUser(goodsInfo.getUpdateUser());
				annexInfo.setUpdateUserId(goodsInfo.getUpdateUserId());
				annexInfo.setAnnexId(IDGenerator.generatorId());
				annexList.add(annexInfo);
			}
			//轮播图
			gsGoodsMapper.deleteGoodsAnnex(goodsInfo.getGoodsId());
			gsGoodsMapper.initGoodsAnnexImgs(annexList);
		}
		
	}
	public void initGoodsAnnex(GsGoodsInsertInfo goodsInfo) {
		String realFilePath = null;
		//处理头像
		Object headPic = goodsInfo.getAnnexUrl();
		String headPortrait = goodsInfo.getAnnexUrlPortrait();
		
		byte[] fileByteArray = null;
		boolean isUpdate = false;

		if (headPic != null && headPic instanceof MultipartFile) {
			MultipartFile file = (MultipartFile) goodsInfo.getAnnexUrl();
			realFilePath = FileSrcUtil.createFileSrc(Type.GOODS, goodsInfo.getGoodsId(), file.getOriginalFilename());
			try {
				fileByteArray = file.getBytes();
				isUpdate = true;
			} catch (IOException e) {
				log.error("文件上传失败", e);
			}

			goodsInfo.setAnnexUrlPortrait(realFilePath);
		} else {
			if(headPortrait!=null) {
				if(headPortrait.startsWith("//")) {
					headPortrait="http:"+headPortrait;
				}
				File file=new File(headPortrait);
				realFilePath = FileSrcUtil.createFileSrc(Type.GOODS, goodsInfo.getGoodsId(), file.getName());
				fileByteArray = ImageUtil.getImageFromNetByUrl(headPortrait);
				isUpdate = true;
			}
		}
		goodsInfo.setAnnexUrlPortrait(realFilePath);
		GsGoodsAnnex annexInfo = new GsGoodsAnnex();
		annexInfo.setGoodsId(goodsInfo.getGoodsId());
		annexInfo.setAnnexUrl(goodsInfo.getAnnexUrlPortrait());
		annexInfo.setGsAnnexType("1"); //封面
		annexInfo.setAnnexName(goodsInfo.getGoodsName()+"封面图");
		annexInfo.setUpdateUser(goodsInfo.getUpdateUser());
		annexInfo.setUpdateUserId(goodsInfo.getUpdateUserId());
		annexInfo.setAnnexId(IDGenerator.generatorId());
		gsGoodsMapper.addGsGoodsAnnex(annexInfo);
		
		String bucket = yzSysConfig.getBucket();
		if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}
	}
	public void updateGoodsAnnex(GsGoodsInsertInfo goodsInfo) {
		boolean isDeleteFile = false;
		String realFilePath = null;
		boolean isUpdate = false;
		byte[] fileByteArray = null;
		if ("1".equals(goodsInfo.getIsPhotoChange())) {
			//处理头像
			Object headPic = goodsInfo.getAnnexUrl();
			String headPortrait = goodsInfo.getAnnexUrlPortrait();

			if (headPic != null && headPic instanceof MultipartFile) {
				MultipartFile file = (MultipartFile) goodsInfo.getAnnexUrl();
				realFilePath = FileSrcUtil.createFileSrc(Type.GOODS, goodsInfo.getGoodsId(), file.getOriginalFilename());
				try {
					fileByteArray = file.getBytes();
					isUpdate = true;
				} catch (IOException e) {
					log.error("文件上传失败", e);
				}

				goodsInfo.setAnnexUrlPortrait(realFilePath);
			} else {
				if(headPortrait!=null&&headPortrait.indexOf("360buyimg.com")>=0) {
					if(headPortrait.startsWith("//")) {
						headPortrait="http:"+headPortrait;
					}
					File file=new File(headPortrait);
					realFilePath = FileSrcUtil.createFileSrc(Type.GOODS, goodsInfo.getGoodsId(), file.getName());
					fileByteArray = ImageUtil.getImageFromNetByUrl(headPortrait);
					isUpdate = true;
					goodsInfo.setAnnexUrlPortrait(realFilePath);
				}else {
					realFilePath = headPortrait;
					goodsInfo.setAnnexUrlPortrait("");
					isDeleteFile = true;
				}
				
			}
		}

		GsGoodsAnnex annexInfo = new GsGoodsAnnex();
		annexInfo.setGoodsId(goodsInfo.getGoodsId());
		annexInfo.setAnnexUrl(goodsInfo.getAnnexUrlPortrait());
		annexInfo.setGsAnnexType("1"); //封面
		annexInfo.setAnnexName(goodsInfo.getGoodsName()+"封面图");
		annexInfo.setUpdateUser(goodsInfo.getUpdateUser());
		annexInfo.setUpdateUserId(goodsInfo.getUpdateUserId());
		
		gsGoodsMapper.updateGsGoodsAnnex(annexInfo);

		String bucket = yzSysConfig.getBucket();
		if (isDeleteFile) {
			FileUploadUtil.delete(bucket, realFilePath);
		} else if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}

	}
	public GsGoodsInsertInfo getGoodsDetailInfo(String goodsId){
		GsGoodsInsertInfo inserInfo = gsGoodsMapper.getGoodsDetailInfo(goodsId);
		if(null != inserInfo){
			if(StringUtil.hasValue(inserInfo.getGoodsUse())){
				inserInfo.setGoodsUses(inserInfo.getGoodsUse().split(";"));
			}
		}
		return inserInfo;
	}
	
	public GsGoodsInsertInfo getActivityGoodsDetailInfo(String goodsId){
		GsGoodsInsertInfo inserInfo = gsGoodsMapper.getActivityGoodsDetailInfo(goodsId);
		if(null != inserInfo){
			if(StringUtil.hasValue(inserInfo.getGoodsUse())){
				inserInfo.setGoodsUses(inserInfo.getGoodsUse().split(";"));
			}
		}
		return inserInfo;
	}
	
	public GsGoodsInsertInfo getCourseGoodsDetailInfo(String goodsId){
		GsGoodsInsertInfo inserInfo = gsGoodsMapper.getCourseGoodsDetailInfo(goodsId);
		if(null != inserInfo){
			if(StringUtil.hasValue(inserInfo.getGoodsUse())){
				inserInfo.setGoodsUses(inserInfo.getGoodsUse().split(";"));
			}
		}
		return inserInfo;
	}

	

 
	
	//获取活动商品状态
	public static String getSalesStatus(String startTime, String endTime)
	{
		String salesStatus = "0";
		Date startDate = DateUtil.convertDateStrToDate(startTime, DateUtil.YYYYMMDDHHMMSS_SPLIT);
		Date endDate = DateUtil.convertDateStrToDate(endTime, DateUtil.YYYYMMDDHHMMSS_SPLIT);

		Date currentDate = new Date();
		if (currentDate.getTime() < startDate.getTime()) {
			salesStatus = "0"; // 即将开始
		} else if (currentDate.getTime() > startDate.getTime() && currentDate.getTime() < endDate.getTime()) {
			salesStatus = "1"; // 进行中
		} else if (currentDate.getTime() > endDate.getTime()) {
			salesStatus = "2"; // 已经结束
		}
		return salesStatus;
	}
    
    public void batchStopGoods(String[] idArray){
    	gsGoodsMapper.batchStopGoods(idArray);
    }
    
    public void batchStartGoods(String[] idArray){
    	gsGoodsMapper.batchStartGoods(idArray);
    }
    public int getGoodsCountById(String goodsId){
    	return gsGoodsMapper.getGoodsCountById(goodsId);
    }
	
}
