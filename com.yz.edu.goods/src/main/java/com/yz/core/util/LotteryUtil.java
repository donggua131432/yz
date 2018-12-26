package com.yz.core.util;

import com.yz.model.GsPrize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LotteryUtil {

	public static int drawGift(List<GsPrize> prizes) {

		if (null != prizes && prizes.size() > 0) {
			List<Double> orgProbList = new ArrayList<Double>(prizes.size());
			for (GsPrize prize : prizes) {
				// 按顺序将概率添加到集合中
				orgProbList.add(prize.getProbability());
			}

			return draw(orgProbList);

		}
		return -1;
	}

	public static int draw(List<Double> giftProbList) {

		List<Double> sortRateList = new ArrayList<Double>();

		// 计算概率总和
		Double sumRate = 0D;
		for (Double prob : giftProbList) {
			sumRate += prob;
		}

		if (sumRate != 0) {
			double rate = 0D; // 概率所占比例
			for (Double prob : giftProbList) {
				rate += prob;
				// 构建一个比例区段组成的集合(避免概率和不为1)
				sortRateList.add(rate / sumRate);
			}

			// 随机生成一个随机数，并排序
			double random = Math.random();
			sortRateList.add(random);
			Collections.sort(sortRateList);

			// 返回该随机数在比例集合中的索引
			return sortRateList.indexOf(random);
		}

		return -1;
	}

}
