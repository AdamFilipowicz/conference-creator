package com.afi.data;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public final class TableData {
	private static final Map<String, TableMapObject> conferenceMap = new LinkedHashMap<String, TableMapObject>();
	private static final Map<String, TableMapObject> prelegentMap = new LinkedHashMap<String, TableMapObject>();
	static
    {
		conferenceMap.put("Nazwa konferencji", data("name", 20, false, "text"));
		conferenceMap.put("Data rozpoczęcia", data("startDate", 9, false, "data"));
		
		prelegentMap.put("Imię", data("name", 10, false, "text"));
		prelegentMap.put("Nazwisko", data("surname", 15, false, "text"));
    }
	public static Map<String, TableMapObject> getConferenceMap() {
		return conferenceMap;
	}
	public static Map<String, TableMapObject> getPrelegentMap() {
		return prelegentMap;
	}
	
	private static TableMapObject data(String tableObjectName, int length, boolean nullable, String type) {
		return new TableMapObject(tableObjectName, length, nullable, type);
	}
}
