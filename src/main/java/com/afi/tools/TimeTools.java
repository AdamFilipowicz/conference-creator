package com.afi.tools;

import java.sql.Timestamp;

public class TimeTools {
	@SuppressWarnings("deprecation")
	public static String formatTime(Timestamp time) {
		String hours = (time.getHours()) >= 10 ? "" + time.getHours() : "0" + time.getHours();
		String minutes = (time.getMinutes()) >= 10 ? "" + time.getMinutes() : "0" + (time.getMinutes());
		return hours + ":" + minutes;
	}
}
