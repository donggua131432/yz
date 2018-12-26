package com.yz.core.util;

import java.util.Date;

import com.yz.constants.StudentConstants;
import com.yz.util.DateUtil;

/**
 * 身份证工具
 * @author Administrator
 *
 */
public class IdCardUtil {

	public static IdInfo convert(String idCard) {
		IdInfo info = new IdInfo();
		
		String provinceCode = idCard.substring(0, 2) + "0000";
		String cityCode = idCard.substring(0, 4) + "00";
		String districtCode = idCard.substring(0, 6);
		
		String bd = idCard.substring(6, 14);
		String newBd = bd.substring(0, 4) + "-" + bd.substring(4, 6) + "-" + bd.substring(6, 8);
		
		
		int sexNum = Integer.valueOf(idCard.substring(16, 17));
		String sex = sexNum % 2 == 0 ? StudentConstants.SEX_WOMEN : StudentConstants.SEX_MEN;

		
		Date date = DateUtil.convertDateStrToDate(newBd, "yyyy-MM-dd");
		//计算年龄
		Date now = new Date();
		long n = now.getTime();
		long d = date.getTime();
		long s = ((n - d) / (1000 * 60 * 60 * 24)) / 365;
		
		info.setProvince(provinceCode);
		info.setCity(cityCode);
		info.setDistrict(districtCode);
		info.setBirthday(newBd);
		info.setAge(s + "");
		info.setSex(sex);
		
		return info;
	}
	
	
	public static class IdInfo {
		private String province;
		private String city;
		private String district;
		private String birthday;
		private String sex;
		private String age;
		
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}
		public String getBirthday() {
			return birthday;
		}
		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getAge() {
			return age;
		}
		public void setAge(String age) {
			this.age = age;
		}
	}
}
