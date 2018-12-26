package com.yz.core.discern;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yz.constants.RecruitConstants;
import com.yz.core.discern.EnrollHttpClientUtil.Result;
import com.yz.model.enroll.BdEnrollOnline;
import com.yz.util.JsonUtil;
import com.yz.util.MethodUtil;
import com.yz.util.StringUtil;

public class EnrollUtil {

	public static EnrollResult enroll(BdEnrollOnline enrollInfo) {
		return enroll(enrollInfo, null);
	}

	/**
	 * 报名
	 * 
	 * @param enrollInfo
	 * @return
	 */
	public static EnrollResult enroll(BdEnrollOnline enrollInfo, String sessionId) {
		Field[] fields = BdEnrollOnline.class.getDeclaredFields();

		String[] ingore = { "hiddenYzdm", "learnId", "status", "unvsCode", "pfsnCode", "teachMethod", "ybmhm", "reason",
				"isCompleted", };

		List<NameValuePair> param = new ArrayList<NameValuePair>();

		String kslbdm = enrollInfo.getKslbdm();// 考生类型

		String hidden_yzdm = enrollInfo.getHiddenYzdm();

		NameValuePair hp = new BasicNameValuePair("hidden_yzdm", hidden_yzdm);// 外语语种
		param.add(hp);

		if (RecruitConstants.PFSN_LEVEL_ZSB.equals(kslbdm)) {
			NameValuePair p1 = new BasicNameValuePair("zsbpc1bkyx1", enrollInfo.getUnvsCode());// 志愿院校
			NameValuePair p2 = new BasicNameValuePair("zsbpc1bkyx1zy1", enrollInfo.getPfsnCode());// 志愿专业
			NameValuePair p3 = new BasicNameValuePair("zsbpcdm1", "1");// ??
			NameValuePair p4 = new BasicNameValuePair("zsbyxsx1", "1");// ??

			param.add(p1);
			param.add(p2);
			param.add(p3);
			param.add(p4);

		} else if (RecruitConstants.PFSN_LEVEL_GQZ.equals(kslbdm)) {

			String teachMethod = enrollInfo.getTeachMethod();

			if (RecruitConstants.TEACH_METHOD_DAYOFF.equals(teachMethod)) {// 脱产
				NameValuePair p1 = new BasicNameValuePair("gqgpc3bkyx1", enrollInfo.getUnvsCode());// 志愿院校
				NameValuePair p2 = new BasicNameValuePair("gqgpc3bkyx1zy1", enrollInfo.getPfsnCode());// 志愿专业

				param.add(p1);
				param.add(p2);
			} else {// 非脱产
				NameValuePair p1 = new BasicNameValuePair("gqgpc4bkyx1", enrollInfo.getUnvsCode());// 志愿院校
				NameValuePair p2 = new BasicNameValuePair("gqgpc4bkyx1zy1", enrollInfo.getPfsnCode());// 志愿专业

				param.add(p1);
				param.add(p2);
			}
		} else {

		}

		for (Field f : fields) {
			boolean need = true;

			String name = f.getName();

			for (String i : ingore) {
				if (i.equals(name)) {
					need = false;
					break;
				}
			}

			try {
				Method get = MethodUtil.getterMethod(BdEnrollOnline.class, name);
				Object value = get.invoke(enrollInfo, null);

				if (value != null && StringUtil.hasValue(value.toString()) && need) {

					String v = value.toString();

					System.err.println("-----------------------" + name + " | " + v);
					NameValuePair p = new BasicNameValuePair(name, v);
					param.add(p);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		System.err.println(" --------------------------- 请求参数" + JsonUtil.object2String(param));
		Result enrollResult = null;
		if ("1".equals(enrollInfo.getCzbj())) {
			enrollResult = EnrollHttpClientUtil.enroll(param, sessionId);
		} else {
			enrollResult = EnrollHttpClientUtil.enroll(param, null);
			sessionId = enrollResult.getSessionId();
		}
		System.err.println(" --------------------------- 报名结果" + enrollResult.getHtml());

		System.err.println("------------------------- sessionId1 :  " + sessionId);

		Result enrollHtmlResult = EnrollHttpClientUtil.enrollResult(sessionId);

		EnrollResult e = enrollResult(enrollHtmlResult);

		if (e.isSuccess()) {
			return e;
		} else {
			return enrollError(enrollResult);
		}
	}

	/**
	 * 验证结果
	 * 
	 * @param enrollInfo
	 * @param sessionId
	 * @return
	 */
	public static ConfirmResult confirm(BdEnrollOnline enrollInfo, String sessionId) {

		List<NameValuePair> values = new ArrayList<NameValuePair>();

		values.add(new BasicNameValuePair("xm", enrollInfo.getXm()));
		values.add(new BasicNameValuePair("ybmh", enrollInfo.getKsid()));
		values.add(new BasicNameValuePair("zjh", enrollInfo.getZjdm()));
		values.add(new BasicNameValuePair("xjxl", "1"));

		Result result = EnrollHttpClientUtil.confirm(values, sessionId);

		ConfirmResult cr = confirmResult2(result);

		if (!cr.isConfirmSuccess) {
			cr = confirm_result(sessionId);
		}

		return cr;
	}

	private static ConfirmResult confirmResult2(Result result) {
		String sucTxt = "验证专科学历通过";
		String repeat = "专科学历已验证完毕，请不要重复验证";

		ConfirmResult cr = new ConfirmResult(false);

		if (result != null) {
			String html = result.getHtml();
			if (StringUtil.isEmpty(html)) {
				cr.setReason("未知异常：页面代码为:::::\n" + html);
				return cr;
			} else {
				Document doc = Jsoup.parse(html);

				Element e = doc.select("script").first();

				if (e == null) {
					cr.setReason("未知异常：页面代码为:::::\n" + html);
					return cr;
				}

				String scriptText = e.toString();

				if (scriptText.indexOf(sucTxt) != -1 || scriptText.indexOf(repeat) != -1) {
					cr.setConfirmSuccess(true);
					return cr;
				}

				int i = scriptText.indexOf("alert(");

				if (i > -1) {
					int end = scriptText.indexOf(");", i);

					String msg = scriptText.substring(i + 6, end);

					cr.setReason(msg);
				} else {
					cr.setReason("未知异常：页面代码为:::::\n" + html);
				}

				return cr;
			}
		}

		cr.setReason("请求数据返回为空");
		return cr;
	}

	/**
	 * 验证结果
	 * 
	 * @param enrollInfo
	 * @param sessionId
	 * @return
	 */
	public static ConfirmResult confirm_result(String sessionId) {

		Result result = EnrollHttpClientUtil.confirmResult(sessionId);

		return confirmResult(result);
	}

	private static EnrollResult enrollError(Result result) {
		if (result != null) {
			String html = result.getHtml();
			String sessionId = result.getSessionId();
			if (StringUtil.isEmpty(html)) {
				return new EnrollResult(false, "请求数据返回为空", sessionId);
			} else {
				Document doc = Jsoup.parse(html);

				Element e = doc.select("script").get(1);

				if (e == null) {
					return new EnrollResult(false, "未知异常：页面代码为:::::\n" + html, sessionId);
				}

				String scriptText = e.toString();

				int i = scriptText.indexOf("alert(");

				if (i > -1) {
					int end = scriptText.indexOf(");", i);

					String msg = scriptText.substring(i + 6, end);

					return new EnrollResult(false, msg, sessionId);
				} else {
					return new EnrollResult(false, "未知异常：页面代码为:::::\n" + html, sessionId);
				}

			}
		}

		return new EnrollResult(false, "请求数据返回为空", null);
	}

	private static EnrollResult enrollResult(Result result) {
		if (result != null) {
			String html = result.getHtml();
			String sessionId = result.getSessionId();
			if (StringUtil.isEmpty(html)) {
				return new EnrollResult(false, "请求数据返回为空", sessionId);
			} else {
				Document doc = Jsoup.parse(html);
				Element rEle = doc.select(".top").first();

				if (rEle != null) {
					String r = rEle.text();

					if ("报名成功".equals(r.trim())) {

						Element strong = doc.select("strong").first();

						Elements fonts = strong.select("font");

						Element e1 = fonts.get(1);
						Element e2 = fonts.get(2);

						String ybmh = e1 == null ? null : e1.text();
						String pwd = e2 == null ? null : e2.text();

						return new EnrollResult(true, ybmh, pwd, sessionId);
					} else {
						return new EnrollResult(false, "请求数据返回为空", sessionId);
					}
				}
			}
		}

		return new EnrollResult(false, "请求数据返回为空", null);
	}

	private static ConfirmResult confirmResult(Result result) {
		String wt = "你已验证3次";
		ConfirmResult cr = new ConfirmResult(false);
		if (result != null) {
			String html = result.getHtml();

			if (StringUtil.isEmpty(html))
				return cr;

			Document doc = Jsoup.parse(html);

			Elements div = doc.select("div").attr("align", "left");

			Element strong = doc.select("strong").first();

			if (strong != null) {
				String ttt = strong.text();
				if(StringUtil.hasValue(ttt)) {
					if(ttt.indexOf(wt) > -1) {
						cr.setCanConfirm(false);
					}
					cr.setReason(ttt);
				}
			}

			if (div == null || div.size() < 2)
				return new ConfirmResult(false);

			Element confirmElement = div.get(1);

			Element font = confirmElement.select("font").first();

			if (font == null)
				return cr;

			String r = font.text();

			if ("合格".equals(r)) {
				cr = new ConfirmResult(true);
			} else {
				cr = new ConfirmResult(false);
			}

			Elements tables = doc.select("table").select("table");
			if (tables == null || tables.size() < 2)
				return cr;

			Element msgTable = tables.get(1);
			if (msgTable == null)
				return cr;

			Elements tr = msgTable.select("tr");

			if (tr == null || tr.isEmpty())
				return cr;

			Iterator<Element> it = tr.iterator();

			while (it.hasNext()) {
				Element tre = it.next();

				Elements tds = tre.select("td");

				if (tds == null || tds.size() < 2)
					return cr;
				String fName = tds.get(0).text() == null ? null : tds.get(0).text().trim();
				String fValue = tds.get(1).text();
				if ("姓  名:".equals(fName)) {
					cr.setName(fValue);
				} else if ("毕业证号:".equals(fName)) {
					cr.setByzjh(fValue);
				} else if ("毕业学校:".equals(fName)) {
					cr.setByxx(fValue);
				} else if ("毕业专业:".equals(fName)) {
					cr.setByzy(fValue);
				} else if ("学历类别:".equals(fName)) {
					cr.setXlcc(fValue);
				} else if ("毕业时间:".equals(fName)) {
					cr.setBysj(fValue);
				} else if ("毕业结论:".equals(fName)) {
					cr.setJl(fValue);
				}
			}

			return cr;

		}

		return new ConfirmResult(false);
	}
	
	public static ChargeResult chargeResult(String sessionId) {
		Result enrollResult = EnrollHttpClientUtil.enrollResult(sessionId);
		ChargeResult cr = chargeResult(enrollResult);
		
		if(cr.isNext) {
			enrollResult = EnrollHttpClientUtil.localConfirm(sessionId);
			return chargeResult(enrollResult);
		}
		
		return cr;
	}

	public static ChargeResult chargeResult(Result result) {
		String cTxt = "您的报名信息已确认";
		if (result != null) {
			String html = result.getHtml();

			Document doc = Jsoup.parse(html);
			
			
			
			Elements scripts = doc.select("script");
			
			if(scripts != null && !scripts.isEmpty()) {
				Iterator<Element> i = scripts.iterator();
				while(i.hasNext()) {
					Element script = i.next();
					if(script == null)
						continue;
					String text = script.toString();
					
					if(StringUtil.isEmpty(text))
						continue;
					
					if(text.indexOf(cTxt) != -1) {
						ChargeResult cr = new ChargeResult(false);
						cr.setNext(true);
						return cr;
					}
				}
			}
			

			Elements tds = doc.select("td").attr("class", "ltd");

			if (tds == null || tds.isEmpty())
				return new ChargeResult(false);

			Iterator<Element> it = tds.iterator();

			while (it.hasNext()) {
				Element td = it.next();
				String s = td.text() == null ? null : td.text().trim();

				if ("交费情况：".equals(s)) {
					Element td2 = td.nextElementSibling();

					String s2 = td2.text();

					if ("已交费".equals(s2)) {
						ChargeResult cr = new ChargeResult(true);

						Iterator<Element> i = tds.iterator();

						while (i.hasNext()) {
							Element tdd = i.next();

							String s3 = tdd.text();
							if ("执收单位：".equals(s3)) {
								Element tdd1 = tdd.nextElementSibling();
								cr.setSfdw(tdd1.text());
							}

							if ("缴款通知书：".equals(s3)) {
								Element tdd1 = tdd.nextElementSibling();
								cr.setJfdzs(tdd1.text());
							}
						}
						return cr;
					} else {
						return new ChargeResult(false);
					}
				}
			}
		}

		return new ChargeResult(false);
	}

	public static LocalResult localConfirm(Result result) {
		if (result != null) {
			String html = result.getHtml();

			Document doc = Jsoup.parse(html);

			Element div = doc.select(".top").first();

			if (div != null) {
				String s = div.text() == null ? null : div.text().trim();

				if ("报名确认成功".equals(s)) {
					return new LocalResult(true);
				}
			} else {
				String err = "您的报名信息尚未确认，无法再进行本页面操作！";
				Elements scripts = doc.select("script");

				if (scripts != null) {
					Iterator<Element> it = scripts.iterator();

					while (it.hasNext()) {
						Element e = it.next();
						String text = e == null ? null : e.toString();
						if (StringUtil.hasValue(text)) {
							if (text.indexOf(err) != -1) {
								return new LocalResult(false, err);
							}
						}
					}
				}
			}
		}

		return new LocalResult(false);
	}

	public static class EnrollResult {
		private boolean isSuccess;
		private String ybmh;
		private String pwd;
		private String reason;
		private String sessionId;

		public EnrollResult(boolean isSuccess, String ybmh, String pwd, String sessionId) {
			this.isSuccess = isSuccess;
			this.ybmh = ybmh;
			this.pwd = pwd;
			this.sessionId = sessionId;
		}

		public EnrollResult(boolean isSuccess, String reason, String sessionId) {
			this.isSuccess = isSuccess;
			this.setReason(reason);
			this.sessionId = sessionId;
		}

		public boolean isSuccess() {
			return isSuccess;
		}

		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}

		public String getYbmh() {
			return ybmh;
		}

		public void setYbmh(String ybmh) {
			this.ybmh = ybmh;
		}

		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public String getSessionId() {
			return sessionId;
		}

		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}

	}

	public static class ConfirmResult {
		private boolean isConfirmSuccess;
		private String name;
		private String byzjh;
		private String byxx;
		private String byzy;
		private String xlcc;
		private String bysj;
		private String jl;
		private String warnTxt;
		
		private boolean canConfirm = true;

		private String reason;

		public ConfirmResult(boolean isConfirmSuccess) {
			this.isConfirmSuccess = isConfirmSuccess;
		}

		public boolean isConfirmSuccess() {
			return isConfirmSuccess;
		}

		public void setConfirmSuccess(boolean isConfirmSuccess) {
			this.isConfirmSuccess = isConfirmSuccess;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getByzjh() {
			return byzjh;
		}

		public void setByzjh(String byzjh) {
			this.byzjh = byzjh;
		}

		public String getByxx() {
			return byxx;
		}

		public void setByxx(String byxx) {
			this.byxx = byxx;
		}

		public String getByzy() {
			return byzy;
		}

		public void setByzy(String byzy) {
			this.byzy = byzy;
		}

		public String getXlcc() {
			return xlcc;
		}

		public void setXlcc(String xlcc) {
			this.xlcc = xlcc;
		}

		public String getBysj() {
			return bysj;
		}

		public void setBysj(String bysj) {
			this.bysj = bysj;
		}

		public String getJl() {
			return jl;
		}

		public void setJl(String jl) {
			this.jl = jl;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}

		public String getWarnTxt() {
			return warnTxt;
		}

		public void setWarnTxt(String warnTxt) {
			this.warnTxt = warnTxt;
		}

		public boolean isCanConfirm() {
			return canConfirm;
		}

		public void setCanConfirm(boolean canConfirm) {
			this.canConfirm = canConfirm;
		}
	}

	public static class LocalResult {
		private boolean isLocalConfirm;

		private String resason;

		public LocalResult(boolean isLocalConfirm) {
			this.isLocalConfirm = isLocalConfirm;
		}

		public LocalResult(boolean isLocalConfirm, String reason) {
			this.isLocalConfirm = isLocalConfirm;
			this.resason = reason;
		}

		public boolean isLocalConfirm() {
			return isLocalConfirm;
		}

		public void setLocalConfirm(boolean isLocalConfirm) {
			this.isLocalConfirm = isLocalConfirm;
		}

		/**
		 * @return the resason
		 */
		public String getResason() {
			return resason;
		}

		/**
		 * @param resason
		 *            the resason to set
		 */
		public void setResason(String resason) {
			this.resason = resason;
		}
	}

	public static class ChargeResult {
		private boolean isCharge;
		private String sfdw;
		private String jfdzs;
		
		private boolean isNext;

		public ChargeResult(boolean isCharge) {
			this.isCharge = isCharge;
		}

		public boolean isCharge() {
			return isCharge;
		}

		public void setCharge(boolean isCharge) {
			this.isCharge = isCharge;
		}

		public String getSfdw() {
			return sfdw;
		}

		public void setSfdw(String sfdw) {
			this.sfdw = sfdw;
		}

		public String getJfdzs() {
			return jfdzs;
		}

		public void setJfdzs(String jfdzs) {
			this.jfdzs = jfdzs;
		}

		public boolean isNext() {
			return isNext;
		}

		public void setNext(boolean isNext) {
			this.isNext = isNext;
		}
	}
}
