package com.afi.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.afi.dto.TableConference;
import com.afi.model.Conference;

@Component
public class ConferenceMapper {

	public TableConference toTableConference(Conference conference) {
		TableConference tableConference = new TableConference();
		tableConference.setId(conference.getId());
		tableConference.setName(conference.getName());
		tableConference.setStartDate(formatDate(conference.getStartDate()));
		return tableConference;
	}

	public Conference toConference(TableConference tableConference) {
		Conference conference = new Conference();
		conference.setId(tableConference.getId());
		conference.setName(tableConference.getName());
		try {
			conference.setStartDate(new Timestamp(
					(new SimpleDateFormat("yyyy-MM-dd").parse(tableConference.getStartDate())).getTime()));
		} 
		catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return conference;
	}

	@SuppressWarnings("deprecation")
	private String formatDate(Timestamp date) {
		String month = (date.getMonth() + 1) >= 10 ? "" + (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
		String day = (date.getDate()) >= 10 ? "" + (date.getDate()) : "0" + (date.getDate());
		return (date.getYear() + 1900) + "-" + month + "-" + day;
	}
}