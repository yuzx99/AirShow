package com.mircobox.util;

import java.util.Calendar;
import java.util.Date;

public class MBDateUtils {
	// 判断是否是今天或是昨天
	public static String whetherTodayOrYesterday(Date date) {
		// TODO Auto-generated method stub
		Calendar current = Calendar.getInstance();
		current.setTime(date);
		Calendar today = Calendar.getInstance(); // 今天

		today.set(Calendar.YEAR, today.get(Calendar.YEAR));
		today.set(Calendar.MONTH, today.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance(); // 昨天

		yesterday.set(Calendar.YEAR, yesterday.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, yesterday.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,
				yesterday.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);
		if (current.after(today)) {
			return "今天 ";
		} else if (current.before(today) && current.after(yesterday)) {

			return "昨天 ";
		} else {
			return null;
		}
	}
}
