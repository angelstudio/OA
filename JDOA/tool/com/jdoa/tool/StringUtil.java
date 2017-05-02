package com.jdoa.tool;

import java.util.Random;

public class StringUtil {

	public static boolean isEmpty(String string) {
		return string == null || string.trim().length() <= 0;
	}

	/**
	 * 获取指定长度随机字符串
	 * 
	 * @author lyle
	 * @param length
	 * @return May 17, 2013
	 */
	public static String getRandomString(int length) {
		String base = "QWERTYUIOPLKJHGFDSAZXCVBNM";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String getParameter(String url, String paras) {
		if (isEmpty(url) || isEmpty(paras)) {
			return "";
		}
		String[] parasArray = url.substring(url.indexOf("?") + 1, url.length())
				.split("&");
		for (int i = 0; i < parasArray.length; i++) {
			String string = parasArray[i];
			String[] eq = string.split("=");
			if (eq.length == 2 && paras.trim().equals(eq[0].trim())) {
				return eq[1].trim();
			}
		}
		return "";
	}

	public static String getStringEmpty(String string) {
		String str = "";
		if (string == null) {

		} else {
			str = string;
		}
		return str;
	}
}
