package com.afi.dto;

public class TableGrade {
	private long id;
	private String grade;
	private String prelegentLecturerName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getPrelegentLecturerName() {
		return prelegentLecturerName;
	}
	public void setPrelegentLecturerName(String prelegentLecturerName) {
		this.prelegentLecturerName = prelegentLecturerName;
	}
}
