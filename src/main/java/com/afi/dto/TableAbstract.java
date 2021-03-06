package com.afi.dto;

public class TableAbstract {
	private long id;
	private String name;
	private String pdfPath;
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
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	public String getPrelegentLecturerName() {
		return prelegentLecturerName;
	}
	public void setPrelegentLecturerName(String prelegentLecturerName) {
		this.prelegentLecturerName = prelegentLecturerName;
	}
}
