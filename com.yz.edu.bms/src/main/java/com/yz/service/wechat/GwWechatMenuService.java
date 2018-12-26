package com.yz.service.wechat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.GwConstants;
import com.yz.dao.wechat.GwWechatMapper;
import com.yz.dao.wechat.GwWechatMenuMapper;
import com.yz.exception.CustomException;
import com.yz.generator.IDGenerator;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.wechat.GwWechatButton;
import com.yz.model.wechat.GwWechatMenu;
import com.yz.model.wechat.GwWechatMenuQuery;
import com.yz.model.wechat.GwWechatPublic;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;

import net.sf.json.JSONObject;

@Service
@Transactional
public class GwWechatMenuService {

	@Autowired
	private GwWechatMenuMapper menuMapper;

	@Autowired
	private GwWechatMapper wechatMapper;

	public IPageInfo<GwWechatMenu> selectWechatMenuByPage(int start, int length, GwWechatMenuQuery query) {
		PageHelper.offsetPage(start, length);
		List<GwWechatMenu> list = menuMapper.selectWechatMenuByPage(query);
		return new IPageInfo<GwWechatMenu>((Page<GwWechatMenu>) list);
	}

	private static String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

	@Value("${wechat_pub.pub_id}")
	private String pubId;
	
	public void setYzWechatMenu() {
		
		GwWechatButton[] buttons = menuMapper.selectMenuByPubId(pubId);

		if (null == buttons || buttons.length <= 0) {
			throw new CustomException("无可用菜单"); // 无可用菜单
		}

		for (GwWechatButton b : buttons) {
			if (GwConstants.WECHAT_MENU_TYPE_VIEW.equals(b.getType())) {
				b.setUrl(b.getKey());
				b.setKey(null);
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("button", buttons);
		
		System.err.println(JsonUtil.object2String(map));
		
		String accessToken = RedisService.getRedisService().get(pubId);

		String sendUrl = url + accessToken;

		// 发送post请求
		String result = HttpUtil.sendPost(sendUrl, JsonUtil.object2String(map),HttpTraceInterceptor.TRACE_INTERCEPTOR);

		JSONObject jobj = JSONObject.fromObject(result);
		String errCode = jobj.getString("errcode");

		if (!"0".equals(errCode)) {
			throw new CustomException("菜单设置异常处理" + jobj.getString("errmsg")); // 菜单设置异常处理
		}

	}
	public GwWechatMenu selectMenuById(String id) {
		return menuMapper.selectByPrimaryKey(id);
	}

	public void addWechatMenu(GwWechatMenu menu) {
		menu.setId(IDGenerator.generatorId());
		menuMapper.insertSelective(menu);
	}

	public void updateWechatMenu(GwWechatMenu menu) {
		menuMapper.updateByPrimaryKeySelective(menu);
	}

	public void deleteWechatMenu(String id) {
		menuMapper.deleteByPrimaryKey(id);
	}

	public void deleteMenus(String[] ids) {
		menuMapper.deleteMenus(ids);
	}

	public IPageInfo<GwWechatPublic> queryWechatPublic(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		List<GwWechatPublic> list = wechatMapper.queryWechatPublic(sqInfo);
		return new IPageInfo<GwWechatPublic>((Page<GwWechatPublic>) list);
	}

	public IPageInfo<GwWechatMenu> sMennu(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		List<GwWechatMenu> list = menuMapper.sMenu(sqInfo);
		return new IPageInfo<GwWechatMenu>((Page<GwWechatMenu>) list);
	}

}
