package com.afi.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afi.dto.TableEvent;
import com.afi.model.Event;
import com.afi.service.LecturerService;
import com.afi.service.PrelegentService;
import com.afi.tools.DateTools;
import com.afi.tools.TimeTools;

@Component
public class EventMapper {
	
	@Autowired
	private PrelegentService prelegentService;
	@Autowired
	private LecturerService lecturerService;

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
		if(event.getLecturer() != null) {
			tableEvent.setPrelegentLecturerName("w "+event.getLecturer().getName()+" "+event.getLecturer().getSurname());
		}
		else if(event.getPrelegent() != null) {
			tableEvent.setPrelegentLecturerName("p "+event.getPrelegent().getName()+" "+event.getPrelegent().getSurname());
		}
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
		if(tableEvent.getPrelegentLecturerName() != null && !"".equals(tableEvent.getPrelegentLecturerName())) {
			String[] splitedPrelegentLecturer = tableEvent.getPrelegentLecturerName().split(" ");
			if("p".equals(splitedPrelegentLecturer[0])) {
				event.setLecturer(null);
				event.setPrelegent(prelegentService.findPrelegentByNameAndSurname(splitedPrelegentLecturer[1], splitedPrelegentLecturer[2]));
			}
			else if("w".equals(splitedPrelegentLecturer[0])) {
				event.setPrelegent(null);
				event.setLecturer(lecturerService.findLecturerByNameAndSurname(splitedPrelegentLecturer[1], splitedPrelegentLecturer[2]));				
			}
		}
		return event;
	}
	
	public String toStringList(Event event) {
		return event.getName();
	}
}
