package com.yz.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yz.util.StringUtil;

/** 
 * 
 * @author lingdian
 * @date 2017年3月7日
 */
public class RegUtils {
	
    /**
     * 	
     * @param regEx 正则表达式 
     * @param targetStr 目标字符串
     * @return
     */
	public static boolean isMatch(String regEx, String targetStr) {
	    return Pattern.matches(regEx, targetStr);
	}
	

	/***
	 * 
	 * @param regEx
	 * @param targetStr
	 * @param callback
	 * @return
	 */
	public static String findAndReplace(String regEx, String targetStr, FindCallback callback) {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(targetStr);
		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			String newStr = callback.execute(m.group());
			if(StringUtil.isNotBlank(newStr))
			m.appendReplacement(buf, newStr);
		}
		m.appendTail(buf);
		return buf.toString();
	}

	/***
	 * 
	 * @param source
	 * @return
	 */
	public static String tranProperty(String source) {
		String reg = "\\_[a-zA-Z\\.0-9\\_\\-\\?\\*\\/\\(\\)]";
		source = StringUtil.lowerCase(source);
		source = findAndReplace(reg, source, new FindCallback() {
			public String execute(String source) {
				source = StringUtil.replace(source, "_", "");
				return StringUtil.upperCase(source);
			}
		});
		return source;
	}

	/**
	 * 
	 * @param template
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String replaceTemplateFormData(String template, Map<String,Object> data)   {
		String regex = "\\$\\{(.+?)\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(template);
		/*
		 * sb用来存储替换过的内容，它会把多次处理过的字符串按源字符串序 存储起来。
		 */
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String name = matcher.group(1);// 键名
			String value = String.valueOf(data.get(name));// 键值

			/*
			 * 由于$出现在replacement中时，表示对捕获组的反向引用，所以要对上面替换内容 中的 $ 进行替换，让它们变成
			 * "\$1000.00" 或 "\$1000000000.00" ，这样 在下面使用
			 * matcher.appendReplacement(sb, value) 进行替换时就不会把 $1
			 * 看成是对组的反向引用了，否则会使用子匹配项值amount 或 balance替换 $1 ，最后会得到错误结果：
			 *
			 * 尊敬的客户刘明你好！本次消费金额amount000.00，您帐户888888888上的余额
			 * 为balance000000.00，欢迎下次光临！
			 *
			 * 要把 $ 替换成 \$ ，则要使用 \\\\\\& 来替换，因为一个 \ 要使用 \\\ 来进 行替换，而一个 $ 要使用
			 * \\$ 来进行替换，因 \ 与 $ 在作为替换内容时都属于 特殊字符：$ 字符表示反向引用组，而 \ 字符又是用来转义 $
			 * 字符的。
			 */
			value = value.replaceAll("\\$", "\\\\\\$");
			// System.out.println("value=" + value);

			/*
			 * 经过上面的替换操作，现在的 value 中含有 $ 特殊字符的内容被换成了"\$1000.00" 或
			 * "\$1000000000.00" 了，最后得到下正确的结果：
			 * 
			 * 尊敬的客户刘明你好！本次消费金额$1000.00，您帐户888888888上的 余额为$1000000.00，欢迎下次光临！
			 * 
			 * 另外，我们在这里使用Matcher对象的appendReplacement()方法来进行替换操作，而
			 * 不是使用String对象的replaceAll()或replaceFirst()方法来进行替换操作，因为
			 * 它们都能只能进行一次性简单的替换操作，而且只能替换成一样的内容，而这里则是要求每
			 * 一个匹配式的替换值都不同，所以就只能在循环里使用appendReplacement方式来进行逐 个替换了。
			 */
			matcher.appendReplacement(sb, value);
		}
		// 最后还得要把尾串接到已替换的内容后面去，这里尾串为“，欢迎下次光临！”
		matcher.appendTail(sb);
		return sb.toString();
	}

	/***
	 * 
	 * @author huang
	 *
	 */
	public static abstract interface FindCallback {
		public abstract String execute(String paramString);
	}
	
	
	public static void main(String[] args) {
		System.out.println(RegUtils.isMatch("com.mhs.trace?", "com.mhs.trace"));
	}
}
