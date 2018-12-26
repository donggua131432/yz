package com.yz.service.finance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.FeeConstants;
import com.yz.dao.finance.BdCouponMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.coupon.BdCoupon;
import com.yz.model.finance.coupon.BdCouponEdit;
import com.yz.model.finance.coupon.BdCouponQuery;
import com.yz.model.finance.coupon.BdCouponResponse;
import com.yz.util.AmountUtil;

@Service
@Transactional
public class BdCouponService {

	@Autowired
	private BdCouponMapper couponMapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object selectCouponByPage(int start, int length, BdCouponQuery coupon) {
		PageHelper.offsetPage(start, length);
		List<BdCoupon> coupons = couponMapper.selectCouponByPage(coupon);
		return new IPageInfo((Page) coupons);
	}

	public void insertCoupon(BdCouponEdit coupon) {
		if (coupon.getItemCodes().length < 1) {
			throw new BusinessException("E000054"); // 科目不能为空
		}
		BigDecimal amount = AmountUtil.str2Amount(coupon.getAmount());
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException("E000049"); // 输入金额不能为负数
		} else {
			coupon.setAmount(AmountUtil.format(amount, 2));
		}
		
		coupon.setCouponId(IDGenerator.generatorId());

		if (FeeConstants.COUPON_TRGGER_TYPE_SCORE.equals(coupon.getcTriggerType())) {

			String low = coupon.getLowestScore();
			String high = coupon.getHighestScore();
			BigDecimal lowScore = AmountUtil.str2Amount(low);
			if (lowScore.compareTo(BigDecimal.ZERO) < 0) {
				throw new BusinessException("E000056"); // 最低分不能小于0
			}
			BigDecimal highScore = AmountUtil.str2Amount(high);
			if (highScore.compareTo(lowScore) < 0) {
				throw new BusinessException("E000057"); // 最高分不能低于最低分
			}

			coupon.setTriggerContent(low + FeeConstants.COUPON_TRIGGER_PREFIX + high);
			
			couponMapper.insertCoupon(coupon);
			
			List<Map<String, String>> list = couponMapper.selectTmpAddPtc(coupon);
			for (Map<String, String> map : list) {
				map.put("ptc_id", IDGenerator.generatorId());
			}
			couponMapper.insertPtfUpdate(list);
			
		} else if (FeeConstants.COUPON_TRGGER_TYPE_REGISTER.equals(coupon.getcTriggerType())) {
			couponMapper.insertRegistCoupon(coupon);
		}

	}

	public Object selectCouponById(String couponId) {
		BdCouponResponse coupon = couponMapper.selectCouponById(couponId);
		if (null != coupon) {
			String triggerType = coupon.getcTriggerType();
			if (FeeConstants.COUPON_TRGGER_TYPE_SCORE.equals(triggerType)) {
				String[] score = coupon.getTriggerContent().split(FeeConstants.COUPON_TRIGGER_PREFIX);
				coupon.setLowestScore(score[0]);
				coupon.setHighestScore(score[1]);
			}
		}
		return coupon;
	}

	public void updateCoupon(BdCouponEdit coupon) {
		BigDecimal amount = AmountUtil.str2Amount(coupon.getAmount());
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException("E000049"); // 输入金额不能为负数
		} else {
			coupon.setAmount(AmountUtil.format(amount, 2));
		}

		if (FeeConstants.COUPON_TRGGER_TYPE_SCORE.equals(coupon.getcTriggerType())) {
			String low = coupon.getLowestScore();
			String high = coupon.getHighestScore();
			BigDecimal lowScore = AmountUtil.str2Amount(low);
			if (lowScore.compareTo(BigDecimal.ZERO) < 0) {
				throw new BusinessException("E000056"); // 最低分不能小于0
			}
			BigDecimal highScore = AmountUtil.str2Amount(high);
			if (highScore.compareTo(lowScore) < 0) {
				throw new BusinessException("E000057"); // 最高分不能低于最低分
			}

			coupon.setTriggerContent(low + FeeConstants.COUPON_TRIGGER_PREFIX + high);
		}

		couponMapper.updateCoupon(coupon);
	}

	public void deleteCoupon(String couponId) {
		String[] couponIds = { couponId };
		couponMapper.deleteCoupons(couponIds);
	}

	public void deleteCoupons(String[] couponIds) {
		couponMapper.deleteCoupons(couponIds);
	}

	public void blockCoupon(String couponId, String status) {
		couponMapper.blockCoupon(couponId, status);
	}

	public boolean validateCouponName(String couponName) {
		int count = couponMapper.selectCouponNameCount(couponName);
		if (count > 0) {
			return false;
		}
		return true;
	}

}
