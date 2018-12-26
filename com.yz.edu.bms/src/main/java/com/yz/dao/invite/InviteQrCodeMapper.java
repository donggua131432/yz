package com.yz.dao.invite;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.condition.invite.InviteQrCodeQuery;
import com.yz.model.invite.InviteQrCodeInfo;

public interface InviteQrCodeMapper {
	/**
	 * 查询二维码推广数据
	 * 
	 * @param queryInfo
	 * @return
	 */
	List<InviteQrCodeInfo> getList(InviteQrCodeQuery queryInfo);
	
	void add(InviteQrCodeInfo info);
	
	void del(@Param("channelIds") String[] channelIds);
	
	InviteQrCodeInfo selectByChancelId(@Param("channelId") String channelId);
	
	int getInviteCount(InviteQrCodeQuery queryInfo);
}
