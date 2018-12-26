package com.yz.core.util;

import java.util.List;

import com.yz.oss.FileUploadInfo;
import com.yz.oss.OSSUtil;
import com.yz.util.StringUtil;

/**
 * 文件上传工具类
 * @author Administrator
 *
 */
public class FileUploadUtil {
	/**
	 * 上传文件
	 * @param fileUploadInfo
	 * @return
	 */
	public static boolean upload(FileUploadInfo fileUploadInfo) {
		String bucket = fileUploadInfo.getBucket();
		String fileSrc = fileUploadInfo.getFileSrc();
		byte[] fileByteArray = fileUploadInfo.getFileByteArray();
		return upload(bucket, fileSrc, fileByteArray);
	}
	
	
	public static boolean upload(String bucket, String fileSrc, byte[] fileByteArray) {
		
		if (StringUtil.isEmpty(bucket) || StringUtil.isEmpty(fileSrc)) {
			return false;
		}
		
		OSSUtil.upload(bucket, fileSrc, fileByteArray);

		return true;
	}
	
	/**
	 * 多文件上传
	 * @param list
	 * @return
	 */
	public static List<String> uploadMore(List<FileUploadInfo> list) {
		return OSSUtil.uploadMore(list);
	}

	/**
	 * 删除文件
	 * @param bucket
	 * @param fileSrc
	 * @return
	 */
	public static boolean delete(String bucket, String fileSrc) {
		if (StringUtil.isEmpty(bucket) || StringUtil.isEmpty(fileSrc)) {
			return false;
		}
		OSSUtil.deleteFile(bucket, fileSrc);
		return true;
	}
	/**
	 * 删除多个文件
	 * @param bucket
	 * @param fileSrcs
	 * @return
	 */
	public static boolean deleteMore(String bucket, List<String> fileSrcs) {

		if (StringUtil.isEmpty(bucket) || fileSrcs == null) {
			return false;
		}
		OSSUtil.deleteFiles(bucket, fileSrcs);
		return true;
	}
	/**
	 * 复制图片
	 * @param oldFileSrc
	 * @param newFileSrc
	 * @return
	 */
	public static boolean copyToDisplay(String oldFileSrc, String newFileSrc) {
		//String newBucket = SysParamUtil.getString(GlobalConstants.FILE_BUCKET);
		//String oldBucket = SysParamUtil.getString(GlobalConstants.TEMP_BUCKET);

		//return OSSUtil.copyFile(oldBucket, oldFileSrc, newBucket, newFileSrc);
		return false;
	}
}
