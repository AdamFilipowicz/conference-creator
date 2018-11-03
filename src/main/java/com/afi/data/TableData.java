package com.afi.data;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public final class TableData {
	public static final String CONFERENCE_IMAGES = "conferenceImages";
	
	private static final Map<String, TableMapObject> abstractMap = new LinkedHashMap<String, TableMapObject>();
	private static final Map<String, TableMapObject> alarmMap = new LinkedHashMap<String, TableMapObject>();
	private static final Map<String, TableMapObject> conferenceMap = new LinkedHashMap<String, TableMapObject>();
	private static final Map<String, TableMapObject> eventMap = new LinkedHashMap<String, TableMapObject>();
	private static final Map<String, TableMapObject> gradeMap = new LinkedHashMap<String, TableMapObject>();
	private static final Map<String, TableMapObject> prelegentMap = new LinkedHashMap<String, TableMapObject>();
	private static final Map<String, TableMapObject> lecturerMap = new LinkedHashMap<String, TableMapObject>();
	static
    {
		abstractMap.put("Nazwa*", data("name", 20, false, "text"));
		abstractMap.put("Plik pdf*", data("pdfPath", 15, false, "pdffile"));
		
		alarmMap.put("Nazwa*", data("name", 20, false, "text"));
		alarmMap.put("Data alarmu*", data("date", 9, false, "data"));
		alarmMap.put("Czas alarmu*", data("time", 9, false, "time"));
		
		conferenceMap.put("Nazwa konferencji*", data("name", 20, false, "text"));
		conferenceMap.put("Data rozpoczęcia*", data("startDate", 9, false, "data"));
		conferenceMap.put("Czas rozpoczęcia*", data("startTime", 9, false, "time"));
		conferenceMap.put("Data zakończenia*", data("endDate", 9, false, "data"));
		conferenceMap.put("Czas zakończenia*", data("endTime", 9, false, "time"));
		
		eventMap.put("Nazwa*", data("name", 20, false, "text"));
		eventMap.put("Typ", data("type", 20, true, "dict"));
		eventMap.put("Data wydarzenia*", data("date", 9, false, "data"));
		eventMap.put("Czas wydarzenia*", data("time", 9, false, "time"));
		
		gradeMap.put("Ocena*", data("grade", 5, false, "int"));
		
		prelegentMap.put("Imię*", data("name", 10, false, "text"));
		prelegentMap.put("Nazwisko*", data("surname", 15, false, "text"));
		prelegentMap.put("Wiek", data("age", 6, true, "int"));
		
		lecturerMap.put("Imię*", data("name", 10, false, "text"));
		lecturerMap.put("Nazwisko*", data("surname", 15, false, "text"));
		lecturerMap.put("Wiek", data("age", 6, true, "int"));
    }
	public static Map<String, TableMapObject> getAbstractMap() {
		return abstractMap;
	}
	public static Map<String, TableMapObject> getAlarmMap() {
		return alarmMap;
	}
	public static Map<String, TableMapObject> getConferenceMap() {
		return conferenceMap;
	}
	public static Map<String, TableMapObject> getEventMap() {
		return eventMap;
	}
	public static Map<String, TableMapObject> getGradeMap() {
		return gradeMap;
	}
	public static Map<String, TableMapObject> getPrelegentMap() {
		return prelegentMap;
	}
	public static Map<String, TableMapObject> getLecturerMap() {
		return lecturerMap;
	}
	
	private static TableMapObject data(String tableObjectName, int length, boolean nullable, String type) {
		return new TableMapObject(tableObjectName, length, nullable, type);
	}
}
