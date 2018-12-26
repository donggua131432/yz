package com.yz.controller.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.FileUploadUtil;
import com.yz.exception.UploadException;
import com.yz.oss.FileUploadInfo;
import com.yz.util.FileUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Controller
@RequestMapping("/file")
public class FileUploadController {

	@Autowired
	private YzSysConfig yzSysConfig;
	
	
	@RequestMapping("/upload")
	@ResponseBody
	@Rule(value={"goods:update"})
	public Object fileUpload(MultipartHttpServletRequest mRequest) {
		String resourceType = mRequest.getParameter("resourceType");
		if (StringUtil.isEmpty(resourceType)) {
			resourceType = GlobalConstants.UPLOAD_RESOURCE_TYPE_SINGLE;
		}
		
		String bucket = yzSysConfig.getTempBucket();
		try {
			if (GlobalConstants.UPLOAD_RESOURCE_TYPE_SINGLE.equals(resourceType)) {
				MultipartFile file = mRequest.getFile("file");
				String fileSrc = createTempUrl(file);
				FileUploadUtil.upload(bucket, fileSrc, file.getBytes());
				return JsonUtil.str2Object(fileSrc);
			} else if(GlobalConstants.UPLOAD_RESOURCE_TYPE_MULTI.equals(resourceType)){
				List<MultipartFile> files = mRequest.getFiles("files");
				if (files == null || files.isEmpty()) {
					throw new UploadException("上传文件不能为空");
				}
				List<String> tempUrls = new ArrayList<String>();
				List<FileUploadInfo> fiList = new ArrayList<FileUploadInfo>();
				for (MultipartFile file : files) {
					String fileSrc = createTempUrl(file);
					fiList.add(new FileUploadInfo(bucket, fileSrc, file.getBytes()));
					tempUrls.add(fileSrc);
				}
				FileUploadUtil.uploadMore(fiList);
				return tempUrls;
			} else {
				throw new UploadException("文件上传失败");
			}
		} catch (Exception e) {
			throw new UploadException("文件上传失败");
		}
	}
	
	@RequestMapping("/webuploader")
	@ResponseBody
	@Rule(value={"goods:update"})
	public void fileUploadForWebuploader(MultipartHttpServletRequest mRequest,HttpServletResponse response) {
		String resourceType = mRequest.getParameter("resourceType");
		if (StringUtil.isEmpty(resourceType)) {
			resourceType = GlobalConstants.UPLOAD_RESOURCE_TYPE_SINGLE;
		}
		
		String bucket = yzSysConfig.getTempBucket();
		try {
			if (GlobalConstants.UPLOAD_RESOURCE_TYPE_SINGLE.equals(resourceType)) {
				MultipartFile file = mRequest.getFile("file");
				String fileSrc = createTempUrl(file);
				FileUploadUtil.upload(bucket, fileSrc, file.getBytes());
			
			    response.setContentType("text/html;charset=UTF-8");
		        response.setHeader("Connection", "close");
		        PrintWriter writer = response.getWriter();
		        writer.write(fileSrc);
		        writer.flush();
		        writer.close();
			}else {
				throw new UploadException("文件上传失败");
			}
		
		} catch (Exception e) {
			throw new UploadException("文件上传失败");
		}
	}

	/**
	 * 创建临时文件访问路径
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private String createTempUrl(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();

		String fileId = StringUtil.UUID();

		String suffix = FileUtil.getSuffix(fileName);

		if (StringUtil.hasValue(suffix)) {
			fileName = fileId + "." + FileUtil.getSuffix(fileName);
		} else {
			fileName = fileId;
		}

		return fileName;
	}
}
