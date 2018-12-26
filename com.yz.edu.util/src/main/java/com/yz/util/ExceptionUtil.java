package com.yz.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

	/**
	 * 获取异常堆栈信息
	 * 
	 * @param ex
	 * @return
	 */
	public static String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		try {
			e.printStackTrace(pw);
			return sw.toString();
		} finally {
			if(pw != null) {
				pw.close();
				pw = null;
			}
		}
	}

}
