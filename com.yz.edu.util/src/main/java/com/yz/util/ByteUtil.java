package com.yz.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ByteUtil {
	/**
	 * 对象转字节数组
	 * 
	 * @param o
	 *            对象
	 * @param checkType
	 *            是否检测基础类型
	 * @return
	 */
	public static byte[] serialize(Object o, boolean checkType) {

		if (o == null)
			return null;

		if (checkType && ClassUtil.isBaseDataType(o.getClass()))
			return o.toString().getBytes();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput op = null;
		try {
			op = new ObjectOutputStream(bos);
			op.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (op != null)
					op.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return bos.toByteArray();
	}
	

	/**
	 * 对象转字节数组
	 * 
	 * @param o
	 * @return
	 */
	public static byte[] serialize(Object o) {
		return serialize(o, false);
	}

	/**
	 * 字节转对象
	 * 
	 * @param b
	 * @return
	 */
	public static Object deSerialize(byte[] b) {
		if (b == null || b.length == 0)
			return null;

		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		ObjectInput oi = null;
		Object o = null;

		try {
			oi = new ObjectInputStream(bis);
			o = oi.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oi != null)
					oi.close();
				if (bis != null)
					bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return o;
	}

	/**
	 * Convert byte[] to hex string. 把字节数组转化为字符串
	 * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv + " ");
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[] 把为字符串转化为字节数组
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 输入流转byte数组
	 * 
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static final byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

}
