package com.yz.core.util;

import com.yz.util.FileUtil;
import com.yz.util.StringUtil;

public class FileSrcUtil {
	/**
	 * 创建文件存放目录
	 * @param type
	 * @param fileName
	 * @return
	 */
	public static String createOrign(Type type,  String fileName) {
		
		StringBuffer s = new StringBuffer(type.get()).append("/");
		s.append(fileName);
		
		return s.toString();
	}

	/**
	 * 创建文件存放目录（重新生成文件名）
	 * 
	 * @param type
	 *            文件前缀
	 * @param id
	 *            对应的ID
	 * @param fileName
	 *            上传的原文件名称
	 * @return
	 */
	public static String createFileSrc(Type type,  String fileName) {
		return createFileSrc(type, null, fileName);
	}
	/**
	 * 创建文件存放目录（重新生成文件名）
	 * 
	 * @param type
	 *            文件前缀
	 * @param id
	 *            对应的ID
	 * @param fileName
	 *            上传的原文件名称
	 * @return
	 */
	public static String createFileSrc(Type type, String id, String fileName) {

		String fileId = StringUtil.UUID().toUpperCase();

		String suffix = FileUtil.getSuffix(fileName);

		if (StringUtil.hasValue(suffix)) {
			fileName = fileId + "." + suffix;
		} else {
			fileName = fileId;
		}

		StringBuffer s = new StringBuffer(type.get()).append("/");
		
		if (StringUtil.hasValue(id)) {
			s.append(id).append("/");
		}
		
		s.append(fileName);
		
		return s.toString();
	}

	/**
	 * 文件目录类型
	 * <table>
	 * <tr>
	 * <th>规则</th>
	 * </tr>
	 * <tr>
	 * <td>STUDENT ：std/{学员ID}/{newFileName}</td>
	 * </tr>
	 * <tr>
	 * <td>COURSE ：course/{课程ID}/{newFileName}</td>
	 * </tr>
	 * <tr>
	 * <td>ORDER ：order/{订单号}/{newFileName}</td>
	 * </tr>
	 * <tr>
	 * <td>EMPLOYEE ：emp/{员工ID}/{newFileName}</td>
	 * </tr>
	 * <tr>
	 * <td>CLASS_PLAN ：pfsn/{专业ID}/{newFileName}</td>
	 * </tr>
	 * </table>
	 * 
	 * @author Administrator
	 *
	 */
	public static enum Type {

		STUDENT("std"), COURSE("course"), ORDER("order"), EMPLOYEE("emp"), CLASS_PLAN("pfsn"), GOODS("gs"),UEDITOR("ue"),BANNER("banner"),SFEXPRESS("sfExpress"),
		CACHE("cache"), MPJAR("mp-jar"), EXCEL("excel"), OUT("out");

		private String name;

		private Type(String name) {
			this.name = name;
		}

		public String get() {
			return this.name;
		}
	}
}
