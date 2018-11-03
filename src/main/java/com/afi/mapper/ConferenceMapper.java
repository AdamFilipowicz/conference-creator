package com.afi.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.afi.dto.TableConference;
import com.afi.model.Conference;
import com.afi.tools.DateTools;
import com.afi.tools.TimeTools;

@Component
public class ConferenceMapper {

	public TableConference toTableConference(Conference conference) {
		TableConference tableConference = new TableConference();
		tableConference.setId(conference.getId());
		tableConference.setName(conference.getName());
		tableConference.setStartDate(DateTools.formatDate(conference.getConferenceStart()));
		tableConference.setStartTime(TimeTools.formatTime(conference.getConferenceStart()));
		tableConference.setEndDate(DateTools.formatDate(conference.getConferenceEnd()));
		tableConference.setEndTime(TimeTools.formatTime(conference.getConferenceEnd()));
		return tableConference;
	}

	public Conference toConference(TableConference tableConference) {
		Conference conference = new Conference();
		conference.setId(tableConference.getId());
		conference.setName(tableConference.getName());
		try {
			conference.setConferenceStart(new Timestamp((new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse(tableConference.getStartDate() + " " + tableConference.getStartTime())).getTime()));
			conference.setConferenceEnd(new Timestamp((new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse(tableConference.getEndDate() + " " + tableConference.getEndTime())).getTime()));
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return conference;
	}
}