package com.afi.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.afi.dto.TableEvent;
import com.afi.model.Event;
import com.afi.tools.DateTools;
import com.afi.tools.TimeTools;

@Component
public class EventMapper {

	public TableEvent toTableEvent(Event event) {
		TableEvent tableEvent = new TableEvent();
		tableEvent.setId(event.getId());
		tableEvent.setName(event.getName());
		if(event.getType() != 0) {
			tableEvent.setType(Integer.toString(event.getType()));
		}
		else {
			tableEvent.setType(null);
		}
		tableEvent.setDate(DateTools.formatDate(event.getEventTime()));
		tableEvent.setTime(TimeTools.formatTime(event.getEventTime()));
		return tableEvent;
	}
	
	public Event toEvent(TableEvent tableEvent) {
		Event event = new Event();
		event.setId(tableEvent.getId());
		event.setName(tableEvent.getName());
		if(tableEvent.getType() != null && !"".equals(tableEvent.getType())) {
			event.setType(Integer.parseInt(tableEvent.getType()));
		}
		try {
			event.setEventTime(new Timestamp((new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse(tableEvent.getDate() + " " + tableEvent.getTime())).getTime()));
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return event;
	}
	
	public String toStringList(Event event) {
		return event.getName();
	}
}
