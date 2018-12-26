package com.yz.controller.system;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.yz.conf.YzSysConfig;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.model.communi.Body;
import com.yz.model.ueditor.ActionEnter;
import com.yz.util.JsonUtil;

/**
 * 百度富文本
 * 
 * @author lx
 * @date 2017年8月5日 上午10:35:43
 */
@Controller
@RequestMapping("/ueditor")
public class UEditorController {

	private final static Logger log = LoggerFactory.getLogger(UEditorController.class);
	
	@Autowired
	private YzSysConfig yzSysConfig ; 

	@RequestMapping(value = "/dispatch")
	public void config(HttpServletRequest request, HttpServletResponse response, String action)
			throws IllegalAccessException, ServletException {
		response.setContentType("application/json");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		try {
			if (action.equals("config")) {
				String exec = new ActionEnter(request, rootPath).exec();
				PrintWriter writer = response.getWriter();
				writer.write(exec);
				writer.flush();
				writer.close();
			} else if (action.equals("uploadimage") || action.equals("listimage")) {
				log.debug("上传图片=" + request.getContentType());
				DefaultMultipartHttpServletRequest dmh = (DefaultMultipartHttpServletRequest) request;
				Object upFile = dmh.getFile("upfile");
				if (upFile != null && upFile instanceof MultipartFile) {
					MultipartFile file = (MultipartFile) upFile;
					String realFilePath = FileSrcUtil.createFileSrc(Type.UEDITOR, file.getOriginalFilename());
					byte[] fileByteArray = file.getBytes();
					String bucket = yzSysConfig.getBucket();
					boolean isSuccess = FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
					log.debug("成功url=" + realFilePath);
					Body body = new Body();
					body.put("state", isSuccess ? "SUCCESS" : "上传失败");
					body.put("url", yzSysConfig.getFileBrowserUrl() + "/" + realFilePath);
					PrintWriter writer = response.getWriter();
					writer.write(JsonUtil.object2String(body));
					writer.flush();
					writer.close();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
