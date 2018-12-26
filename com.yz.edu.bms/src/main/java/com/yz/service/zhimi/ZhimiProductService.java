package com.yz.service.zhimi;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.invite.InviteUserMapper;
import com.yz.dao.zhimi.ZhimiProductMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.zhimi.ZhimiProductQuery;
import com.yz.model.condition.zhimi.ZhimiProductRecordsQuery;
import com.yz.model.zhimi.ZhimiProductInfo;
import com.yz.model.zhimi.ZhimiProductList;
import com.yz.model.zhimi.ZhimiProductRecords;
import com.yz.util.CodeUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class ZhimiProductService {

	@Autowired
	private ZhimiProductMapper productMapper;

	@Autowired
	private InviteUserMapper inviteUserMapper;

	public IPageInfo<ZhimiProductList> getProductList(ZhimiProductQuery queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<ZhimiProductList> list = productMapper.getProductList(queryInfo);

		if (list == null)
			return null;

		return new IPageInfo<ZhimiProductList>((Page<ZhimiProductList>) list);
	}

	public ZhimiProductInfo getProductInfo(String productId) {
		return productMapper.getProductInfo(productId);
	}

	public void add(ZhimiProductInfo productInfo) {
		String price = productInfo.getPrice();
		if (StringUtil.hasValue(price)) {
			BigDecimal b = new BigDecimal(price);
			productInfo.setPrice(b.setScale(2, BigDecimal.ROUND_DOWN).toString());
		}

		productMapper.sortByAdd(productInfo);
		productInfo.setProductId(IDGenerator.generatorId());
		productMapper.addProduct(productInfo);
	}

	public void update(ZhimiProductInfo productInfo) {
		String price = productInfo.getPrice();
		if (StringUtil.hasValue(price)) {
			BigDecimal b = new BigDecimal(price);
			productInfo.setPrice(b.setScale(2, BigDecimal.ROUND_DOWN).toString());
		}

		productMapper.sortByUpdate(productInfo);
		productMapper.updateProduct(productInfo);
	}

	public boolean isExsit(ZhimiProductInfo productInfo) {
		int count = productMapper.countBy(productInfo);
		return count < 1;
	}

	public int getListCount() {
		return productMapper.getListCount();
	}

	@Autowired
	private InviteUserMapper userMapper;

	public IPageInfo<ZhimiProductRecords> getProductRecords(ZhimiProductRecordsQuery queryInfo) {
		if (StringUtil.hasValue(queryInfo.getNickname()) || StringUtil.hasValue(queryInfo.getMobile())
				|| StringUtil.hasValue(queryInfo.getYzCode()) || StringUtil.hasValue(queryInfo.getRealName())) {
			List<String> userIds = inviteUserMapper.getUserIds(queryInfo);
			queryInfo.setUserIds(userIds);
		}

		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<ZhimiProductRecords> list = productMapper.getProductRecords(queryInfo);

		if (list == null)
			return null;

		Map<String, Map<String, String>> exsit = new HashMap<String, Map<String, String>>();

		for (ZhimiProductRecords r : list) {
			String userId = r.getUserId();
			Map<String, String> userInfo = null;
			if (!exsit.containsKey(userId)) {
				userInfo = userMapper.getUserInfo(userId);
			} else {
				userInfo = exsit.get(userId);
			}

			if (userInfo != null) {
				String nickname = userInfo.get("nickname");
				if (StringUtil.hasValue(nickname)) {
					try {
						r.setNickname(CodeUtil.base64Decode2String(nickname));
					} catch (Exception e) {
						r.setNickname(nickname);
					}
				}

				r.setRealName(userInfo.get("name"));
				r.setMobile(userInfo.get("mobile"));
				r.setYzCode(userInfo.get("yzCode"));
				r.setIdCard(userInfo.get("idCard"));
			}
		}

		return new IPageInfo<ZhimiProductRecords>((Page<ZhimiProductRecords>) list);
	}

}
