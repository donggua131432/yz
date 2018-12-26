package com.yz.service.invite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.zxing.WriterException;
import com.yz.conf.YzSysConfig;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.invite.InviteQrCodeMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.invite.InviteQrCodeQuery;
import com.yz.model.invite.InviteQrCodeInfo;
import com.yz.util.QRCodeUtil;

/**
 * 二维码管理
 * 
 * @ClassName: InviteQrCodeService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhanggh
 * @date 2018年5月18日
 *
 */
@Transactional
@Service
public class InviteQrCodeService {

	private static final Logger log = LoggerFactory.getLogger(InviteQrCodeService.class);

	@Autowired
	private InviteQrCodeMapper inviteQrCodeMapper;

	@Autowired
	private YzSysConfig yzSysConfig;

	/**
	 * 根据查询条件获取推广二维码
	 * 
	 * @param queryInfo
	 * @return
	 */
	public Object getList(InviteQrCodeQuery queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength()).setCountMapper("com.yz.dao.invite.InviteQrCodeMapper.getInviteCount");
		List<InviteQrCodeInfo> list = inviteQrCodeMapper.getList(queryInfo);
		return new IPageInfo((Page) list);
	}

	public String generateQrcode(String empUser, String url) {
		String imgurl = "";
		try {
			imgurl=createImageSrc(empUser);
			String bucket = yzSysConfig.getBucket();
			ByteArrayOutputStream outputStream = QRCodeUtil.createQrcodeToStream(url);
			FileUploadUtil.upload(bucket,imgurl, outputStream.toByteArray());
		} catch (WriterException | IOException e) {
			log.error("createQrCodeError: {} create bad!", url);
			imgurl="";
		}
		return imgurl;
	}
	
	private String createImageSrc(String empUser) {
		String name = "invite/qrcode/" + empUser + com.yz.util.StringUtil.UUID()+".png";
		return name;
	}

	public void add(InviteQrCodeInfo info) {
		inviteQrCodeMapper.add(info);
	}
	
	public void del(String[] channelIds) {
		inviteQrCodeMapper.del(channelIds);
	}
	
	public InviteQrCodeInfo selectByChancelId(String channelId) {
		return inviteQrCodeMapper.selectByChancelId(channelId);
	}
}
