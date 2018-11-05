package com.afi.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afi.dto.TableAlarm;
import com.afi.model.Alarm;
import com.afi.service.EventService;
import com.afi.tools.DateTools;
import com.afi.tools.TimeTools;

@Component
public class AlarmMapper {
	
	@Autowired
	private EventService eventService;

	public TableAlarm toTableAlarm(Alarm alarm) {
		TableAlarm tableAlarm = new TableAlarm();
		tableAlarm.setId(alarm.getId());
		tableAlarm.setName(alarm.getName());
		tableAlarm.setDate(DateTools.formatDate(alarm.getAlarmTime()));
		tableAlarm.setTime(TimeTools.formatTime(alarm.getAlarmTime()));
		if(alarm.getEvent() != null) {
			tableAlarm.setEventName(alarm.getEvent().getName());
		}
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
		if(tableAlarm.getEventName() != null && !"".equals(tableAlarm.getEventName())) {
			alarm.setEvent(eventService.findEventByName(tableAlarm.getEventName()));
		}
		return alarm;
	}
}
