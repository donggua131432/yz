package com.yz.service.operate;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.operate.BdBannerMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.exception.SystemException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.operate.banner.BdBanner;
import com.yz.model.operate.banner.BdBannerQuery;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdBannerService {

	@Autowired
	private BdBannerMapper bannerMapper;
	
	@Autowired
	private YzSysConfig yzSysConfig ; 

	public IPageInfo<BdBanner> selectBannerByPage(int start, int length, BdBannerQuery banner) {
		PageHelper.offsetPage(start, length);
		List<BdBanner> banners = bannerMapper.selectBannerByPage(banner);
		return new IPageInfo<BdBanner>((Page<BdBanner>) banners);
	}

	/**
	 * 更新附属信息
	 * 
	 * @param other
	 * @throws IOException 
	 */
	public void updatePic(BdBanner banner) {
		boolean isDeleteFile = false;
		boolean isUpdate = false;
		String realFilePath = null;
		byte[] fileByteArray = null;
		String bannerUrl = banner.getBannerUrl();

		if (GlobalConstants.TRUE.equals(banner.getIsPhotoChange())) {
			Object headPic = banner.getBannerPic();

			if (headPic != null && headPic instanceof MultipartFile) {
				MultipartFile file = (MultipartFile) headPic;
				BufferedImage img;
				try {
					img = ImageIO.read(file.getInputStream());
					if(img.getWidth() > 10000 || img.getHeight() > 2000){
						throw new BusinessException("E000070");	//图片尺寸过大
					}
				} catch (IOException e1) {
					throw new BusinessException("E000070");	//图片尺寸过大
				}
				
				
				if (StringUtil.isEmpty(bannerUrl)) {
					realFilePath = FileSrcUtil.createFileSrc(Type.BANNER, banner.getBannerId(),
							file.getOriginalFilename());
				} else {
					realFilePath = bannerUrl;
				}

				try {
					fileByteArray = file.getBytes();
				} catch (IOException e) {
					throw new SystemException(e.getMessage(), e);
				}
				isUpdate = true;
			} else {
				realFilePath = bannerUrl;
				isDeleteFile = true;
			}
		}
		if (!isDeleteFile && isUpdate) {

			BdBanner bd = new BdBanner();
			BaseUser user = SessionUtil.getUser();
			bd.setBannerId(banner.getBannerId());
			bd.setBannerUrl(realFilePath);
			bd.setUpdateUser(user.getRealName());
			bd.setUpdateUserId(user.getUserId());
			bannerMapper.updateBannerUrl(bd);
		}
		String bucket = yzSysConfig.getBucket();
		if (isDeleteFile) {
			FileUploadUtil.delete(bucket, realFilePath);
		} else if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}

	}

	public void addBanner(BdBanner banner) {
		banner.setBannerId(IDGenerator.generatorId());
		bannerMapper.insertBanner(banner);
		bannerMapper.sortBanner(banner.getBannerId(), banner.getSort());
		updatePic(banner);
	}

	public BdBanner selectBannerById(String bannerId) {
		return bannerMapper.selectBannerById(bannerId);
	}

	public void updateBanner(BdBanner banner) {
		BdBanner bd = bannerMapper.selectBannerById(banner.getBannerId());
		if (bd.getSort() != banner.getSort()) {
			bannerMapper.sortBanner(banner.getBannerId(), banner.getSort());
		}
		bannerMapper.updateBanner(banner);
		updatePic(banner);
	}

	public void updateBannerAllow(BdBanner banner) {
		bannerMapper.updateBannerAllow(banner);
	}

	public int selectBannerAllowCount() {
		return bannerMapper.selectBannerAllowCount();
	}

	public void deleteBanner(String bannerId) {
		String[] bannerIds = { bannerId };
		deleteBanners(bannerIds);
	}

	public void deleteBanners(String[] bannerIds) {
		List<String> bannerUrls = bannerMapper.selectBannerUrls(bannerIds);
		if (null != bannerUrls && bannerUrls.size() > 0) {
			String bucket = yzSysConfig.getBucket();
			FileUploadUtil.deleteMore(bucket, bannerUrls);
		}
		bannerMapper.deleteBanners(bannerIds);
	}

}
