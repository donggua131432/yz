package com.yz.dao.operate;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.operate.banner.BdBanner;
import com.yz.model.operate.banner.BdBannerQuery;

public interface BdBannerMapper {

	List<BdBanner> selectBannerByPage(BdBannerQuery banner);

	int updateBannerUrl(BdBanner bd);

	int insertBanner(BdBanner banner);

	int sortBanner(@Param("bannerId") String bannerId, @Param("sort") String sort);

	BdBanner selectBannerById(String bannerId);

	int updateBanner(BdBanner banner);

	int updateBannerAllow(BdBanner banner);

	int selectBannerAllowCount();

	int deleteBanners(@Param("bannerIds") String[] bannerIds);

	List<String> selectBannerUrls(@Param("bannerIds") String[] bannerIds);

}