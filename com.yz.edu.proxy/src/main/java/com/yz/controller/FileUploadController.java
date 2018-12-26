package com.yz.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yz.conf.YzSysConfig;
import com.yz.core.constants.AppConstants;
import com.yz.core.handler.WechatLoginCheckHandler;
import com.yz.core.util.FileUploadUtil;
import com.yz.core.util.RequestUtil;
import com.yz.exception.CustomException;
import com.yz.exception.UploadException;
import com.yz.model.YzServiceInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.model.communi.Request;
import com.yz.model.communi.Response;
import com.yz.oss.FileUploadInfo;
import com.yz.util.CodeUtil;
import com.yz.util.JWTUtil;
import com.yz.util.StringUtil;

@RestController
public class FileUploadController {
	
	private static WechatLoginCheckHandler loginCheckHandler = new WechatLoginCheckHandler();
	
	@Autowired
	private YzSysConfig yzSysConfig;

	private static YzServiceInfo fielInterfaceInfo = new YzServiceInfo();

	static {
		fielInterfaceInfo.setNeedLogin(true);
	}
 

	@RequestMapping("/uploadFile")
	public Object uploadFile(HttpServletRequest mRequest) throws Throwable {
		
		String file = mRequest.getParameter("file");
		
		String[] files = mRequest.getParameterValues("files");
		
		Response response = null;
		if (file == null && files == null) {
			throw new UploadException("上传文件为空");
		} else {

			String auth = RequestUtil.getHeader("auth");
			if (StringUtil.hasValue(auth)) {
				JWTUtil.Claims claims = JWTUtil.parseJwt(auth);
				String jwtToken = claims.getJwtToken();
				RequestUtil.setAttribute(AppConstants.PUB_JWT_TOKEN, jwtToken);
			}

			Header header = new Header();
			header.setUserId(RequestUtil.getString("userId"));
			header.setAppType(RequestUtil.getString("appType"));
			
			Body body = new Body();

			Request mxdRequest = new Request(header, body);
 

			try {
				loginCheckHandler.check(fielInterfaceInfo, mxdRequest, mRequest);
				if (file != null) {
					response = new Response(uploadFile(file));
				} else {
					response = new Response(uploadFiles(files));
				}
			} catch (Throwable e) {
				throw e;
			} 
		}

		return response;
	}

	/**
	 * 单个文件上传
	 * @param file
	 * @return
	 * @throws Throwable
	 */
	private String uploadFile(String file) throws Throwable {
		
		int i = file.indexOf(",");
		
		String suffix = null;
		String fileData = null;
		if(i > 5) {
			throw new CustomException("请输入正确的文件前缀");
		} else if(i >= 0) {
			suffix = file.substring(0, i);
			fileData = file.substring(i + 1, file.length());
		} else {
			fileData = file;
		}

		String fileId = StringUtil.UUID();

		String filename = fileId;
		
		if(StringUtil.hasValue(suffix)) {
			suffix = suffix.trim();
			filename = fileId + "." + suffix;// UUID + 前缀
		}

		String bucket = yzSysConfig.getTempBucket();

		byte[] fileByte = CodeUtil.base64Decode(fileData);
		
		FileUploadUtil.upload(bucket, filename, fileByte);

		return filename;
	}
	/**
	 * 多文件上传
	 * @param mFiles
	 * @return
	 * @throws Throwable
	 */
	private Object uploadFiles(String[] files) throws Throwable {
		List<FileUploadInfo> uploadInfos = new ArrayList<FileUploadInfo>();
		String bucket = yzSysConfig.getTempBucket();
		for (String file : files) {
			
			int i = file.indexOf(",");
			
			String suffix = null;
			String fileData = null;
			if(i > 5) {
				throw new CustomException("请输入正确的文件前缀");
			} else if(i >= 0) {
				suffix = file.substring(0, i);
				fileData = file.substring(i + 1, file.length());
			} else {
				fileData = file;
			}
			
			String fileId = StringUtil.UUID();
			
			String filename = fileId;
			
			if(StringUtil.hasValue(suffix)) {
				suffix = suffix.trim();
				filename = fileId + "." + suffix;// UUID + 前缀
			}
			
			byte[] fileByte = CodeUtil.base64Decode(fileData);

			uploadInfos.add(new FileUploadInfo(bucket, filename, fileByte));
		}

		return FileUploadUtil.uploadMore(uploadInfos);
	}
 
}
