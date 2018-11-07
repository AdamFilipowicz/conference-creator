package com.afi.data;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public final class TableData {
	public static final String PATH = "C:\\kreator_konferencji\\nowa";
	public static final String EMAIL = "example@ex.com";
	public static final String PHONE = "+48 123 456 789";
	public static final String ADDRESS1 = "ul. Zmyślona 15/20";
	public static final String ADDRESS2 = "12-345 Wrocław";
	public static final String MAP = "51.1000;17.0333";
	
	public static final String CONFERENCE_IMAGES = "zdjecia_konferencji";
	public static final String PDF_FILES = "pdf";
	
	public static final String LOGO = "logo";
	public static final String MAIN_PHOTO = "zdjecie";
	
	//Errors
	public static final String EMPTY_VALUE = "Wartość nie może być pusta";
	public static final String DATE_ERROR = "Data musi być postaci yyyy-MM-dd.\nPrzykładowa data: 2018-08-30";
	public static final String TIME_VALUE_ERROR = "Błędna wartość godzinna lub minutowa";
	public static final String TIME_ERROR = "Czas musi być postaci hh:mm.\nPrzykładowe czasy: 0:00, 8:45, 15:20";
	public static final String VALUE_ERROR = "Podaj poprawną wartość liczbową";
	public static final String SAVE_CHANGES = "Zapisz najpierw zmiany";
	public static final String SAVE_CONFERENCE = "Zapisz najpierw konferencję";
	public static final String SELECT_CONFERENCE = "Wybierz konferencję";
	public static final String IMAGES_ERROR = "Błąd dodawania zdjęć";
	public static final String LOGO_ERROR = "Błąd dodawania logo";
	public static final String IMAGE_ERROR = "Błąd dodawania głównego zdjęcia";
	public static final String IMAGE_READ_ERROR = "Nie wczytano zdjęcia";
	
	//Info
	public static final String CONFIG_CHANGED = "Pomyślnie zmieniono konfigurację";
	public static final String IMAGES_ADD_INFO = "Pomyślnie dodano zdjęcia";
	public static final String LOGO_ADD_INFO = "Pomyślnie ustawiono logo";
	public static final String IMAGE_ADD_INFO = "Pomyślnie ustawiono główne zdjęcie";
	
	//Questions
	public static final String CONFERENCE_DELETE = "Czy na pewno usunąć konferencję? \nJest to nieodwracalne i usunie to wszystkie pozostałe dane!";
	public static final String CONFERENCE_DELETE_TITLE = "Usuwanie konferencji";
	public static final String CONFERENCE_PLACE = "Czy na pewno ustawić nowe miejsce konferencji?";
	public static final String CONFERENCE_PLACE_TITLE = "Zmiana miejsca konferencji";
	
	//Formats
	public static final String TIME_FORMAT = "hh:mm";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	//Main info text
	public static final String ABSTRACTS_TEXT = "Edytuj abstrakty";
	public static final String ALARM_TEXT = "Edytuj alarmy";
	public static final String CONFERENCE_TEXT = "Edytuj konferencje";
	public static final String CONFIG_TEXT = "Edytuj abstrakty";
	public static final String EVENT_TEXT = "Edytuj wydarzenia";
	public static final String GRADE_TEXT = "Edytuj oceny";
	public static final String LECTURER_TEXT = "Edytuj prelegentów";
	public static final String PRELEGENT_TEXT = "Edytuj wykładowców";
	public static final String PHOTOS_TEXT = "Edytuj zdjęcia konferencji";
	public static final String MAP_TEXT = "Miejsce konferencji";
	public static final String CONTACT_TEXT = "Edytuj kontakt konferencji";
			
	
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
		abstractMap.put("Plik pdf*", data("pdfPath", 30, false, "pdffile"));
		abstractMap.put("Wybór prelegenta/wykładowcy", data("prelegentLecturerName", 20, true, "lecturerPrelegent"));
		
		alarmMap.put("Nazwa*", data("name", 20, false, "text"));
		alarmMap.put("Data alarmu*", data("date", 9, false, "data"));
		alarmMap.put("Czas alarmu*", data("time", 9, false, "time"));
		alarmMap.put("Wydarzenie", data("eventName", 15, true, "event"));
		
		conferenceMap.put("Nazwa konferencji*", data("name", 20, false, "text"));
		conferenceMap.put("Data rozpoczęcia*", data("startDate", 9, false, "data"));
		conferenceMap.put("Czas rozpoczęcia*", data("startTime", 9, false, "time"));
		conferenceMap.put("Data zakończenia*", data("endDate", 9, false, "data"));
		conferenceMap.put("Czas zakończenia*", data("endTime", 9, false, "time"));
		
		eventMap.put("Nazwa*", data("name", 20, false, "text"));
		eventMap.put("Typ", data("type", 7, true, "dict"));
		eventMap.put("Data wydarzenia*", data("date", 9, false, "data"));
		eventMap.put("Czas wydarzenia*", data("time", 9, false, "time"));
		eventMap.put("Wybór prelegenta/wykładowcy", data("prelegentLecturerName", 20, true, "lecturerPrelegent"));
		
		gradeMap.put("Ocena*", data("grade", 5, false, "int"));
		gradeMap.put("Wybór prelegenta/wykładowcy", data("prelegentLecturerName", 20, true, "lecturerPrelegent"));
		
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
