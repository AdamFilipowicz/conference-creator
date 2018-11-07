package com.afi.dto;

public class TableEvent {
	private long id;
	private String name;
	private String type;
	private String date;
	private String time;
	private String prelegentLecturerName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPrelegentLecturerName() {
		return prelegentLecturerName;
	}
	public void setPrelegentLecturerName(String prelegentLecturerName) {
		this.prelegentLecturerName = prelegentLecturerName;
	}
}
