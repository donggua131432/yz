package com.yz.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

public class FileUtil {
	/**
	 * 获取文件后缀名
	 * 
	 * @param file
	 * @return
	 */
	public static String getSuffix(File file) {
		if (file == null)
			return null;
		String fileName = file.getName();
		return getSuffix(fileName);
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getSuffix(String fileName) {
		if (StringUtil.hasValue(fileName)) {
			if (fileName.indexOf(".") > 0) {
				String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				return suffix;
			}
		}
		return null;
	}

	/**
	 * 将字符串写入文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static void writeToJson(String filePath, String object) throws IOException {
		File file = new File(filePath);
		char[] stack = new char[1024];
		int top = -1;

		StringBuffer sb = new StringBuffer();
		char[] charArray = object.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if ('{' == c || '[' == c) {
				stack[++top] = c;
				sb.append("\n" + charArray[i] + "\n");
				for (int j = 0; j <= top; j++) {
					sb.append("\t");
				}
				continue;
			}
			if ((i + 1) <= (charArray.length - 1)) {
				char d = charArray[i + 1];
				if ('}' == d || ']' == d) {
					top--;
					sb.append(charArray[i] + "\n");
					for (int j = 0; j <= top; j++) {
						sb.append("\t");
					}
					continue;
				}
			}
			if (',' == c) {
				sb.append(charArray[i] + "");
				for (int j = 0; j <= top; j++) {
					sb.append("");
				}
				continue;
			}
			sb.append(c);
		}
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		write.write(sb.toString().toCharArray());
		write.flush();
		write.close();
	}

	public static byte[] toByte(File file) {
		try {
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
