package com.jdoa.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	/**
	 * @author Action
	 * @param date
	 * @param format
	 * @describe 获取文本日期 yy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatDateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

}
