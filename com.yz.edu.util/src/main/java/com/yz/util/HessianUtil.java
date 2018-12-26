package com.yz.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.yz.util.ClassUtil;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * 序列化工具
 * @author cyf
 *
 */
public class HessianUtil {

	public static byte[] serialize(Object o, boolean checkType) {
		if (o == null)
			return null;
		
		if(checkType && ClassUtil.isBaseDataType(o.getClass()))
			return o.toString().getBytes();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		HessianOutput ho = new HessianOutput(bos);

		try {
			ho.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ho != null) {
					ho.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return bos.toByteArray();
	}
	
	public static Object deSerialize(byte[] bs) {
		if (bs == null || bs.length == 0)
			return null;

		ByteArrayInputStream bis = new ByteArrayInputStream(bs);
		HessianInput hi = new HessianInput(bis);
		Object o = null;
		try {
			o = hi.readObject();
		} catch (IOException e) {
			o = new String(bs);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (hi != null) {
					hi.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return o;
	}
}
