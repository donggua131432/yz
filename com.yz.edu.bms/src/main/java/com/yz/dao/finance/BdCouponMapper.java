package com.yz.dao.finance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.coupon.BdCoupon;
import com.yz.model.finance.coupon.BdCouponEdit;
import com.yz.model.finance.coupon.BdCouponQuery;
import com.yz.model.finance.coupon.BdCouponResponse;
import com.yz.model.finance.coupon.BdStdCoupon;

public interface BdCouponMapper {

	List<BdCoupon> selectCouponByPage(BdCouponQuery coupon);

	int insertCoupon(BdCouponEdit coupon);

	BdCouponResponse selectCouponById(String couponId);

	int updateCoupon(BdCouponEdit coupon);

	int deleteCoupons(@Param("couponIds") String[] couponIds);

	int blockCoupon(@Param("couponId") String couponId, @Param("status") String status);

	List<BdCoupon> selectAvailableScoreCoupon(String learnId);

	int insertStdCoupon(BdStdCoupon stdCoupon);

	int insertRegistCoupon(BdCouponEdit coupon);

	int selectCouponNameCount(String couponName);

	int deleteCouponByStdId(@Param("stdId") String stdId, @Param("couponIds") String[] couponIds);

	List<BdCoupon> selectAvailableScoreCouponResetOrder(String learnId);

	int deleteAllCouponByStdId(String stdId);

	List<Map<String, String>> selectTmpAddPtc(BdCouponEdit coupon);

	void insertPtfUpdate(@Param("list") List<Map<String, String>> list);

}
