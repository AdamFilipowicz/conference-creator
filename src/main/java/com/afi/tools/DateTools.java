package com.afi.tools;

import java.sql.Timestamp;

public class DateTools {
	@SuppressWarnings("deprecation")
	public static String formatDate(Timestamp date) {
		String month = (date.getMonth() + 1) >= 10 ? "" + (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
		String day = (date.getDate()) >= 10 ? "" + (date.getDate()) : "0" + (date.getDate());
		return (date.getYear() + 1900) + "-" + month + "-" + day;
	}
}
