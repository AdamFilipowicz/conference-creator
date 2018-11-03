package com.afi.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.afi.dto.TableAlarm;
import com.afi.model.Alarm;
import com.afi.tools.DateTools;
import com.afi.tools.TimeTools;

@Component
public class AlarmMapper {

	public TableAlarm toTableAlarm(Alarm alarm) {
		TableAlarm tableAlarm = new TableAlarm();
		tableAlarm.setId(alarm.getId());
		tableAlarm.setName(alarm.getName());
		tableAlarm.setDate(DateTools.formatDate(alarm.getAlarmTime()));
		tableAlarm.setTime(TimeTools.formatTime(alarm.getAlarmTime()));
		return tableAlarm;
	}
	
	public Alarm toAlarm(TableAlarm tableAlarm) {
		Alarm alarm = new Alarm();
		alarm.setId(tableAlarm.getId());
		alarm.setName(tableAlarm.getName());
		try {
			alarm.setAlarmTime(new Timestamp((new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse(tableAlarm.getDate() + " " + tableAlarm.getTime())).getTime()));
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return alarm;
	}
}
