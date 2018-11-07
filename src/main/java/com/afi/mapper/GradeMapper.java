package com.afi.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afi.dto.TableGrade;
import com.afi.model.Grade;
import com.afi.service.LecturerService;
import com.afi.service.PrelegentService;

@Component
public class GradeMapper {
	
	@Autowired
	private PrelegentService prelegentService;
	@Autowired
	private LecturerService lecturerService;

	public TableGrade toTableGrade(Grade grade) {
		TableGrade tableGrade = new TableGrade();
		tableGrade.setId(grade.getId());
		if(grade.getGrade() != 0) {
			tableGrade.setGrade(Integer.toString(grade.getGrade()));
		}
		else {
			tableGrade.setGrade(null);
		}
		if(grade.getLecturer() != null) {
			tableGrade.setPrelegentLecturerName("w "+grade.getLecturer().getName()+" "+grade.getLecturer().getSurname());
		}
		else if(grade.getPrelegent() != null) {
			tableGrade.setPrelegentLecturerName("p "+grade.getPrelegent().getName()+" "+grade.getPrelegent().getSurname());
		}
		return tableGrade;
	}
	
	public Grade toGrade(TableGrade tableGrade) {
		Grade grade = new Grade();
		grade.setId(tableGrade.getId());
		if(tableGrade.getGrade() != null && !"".equals(tableGrade.getGrade())) {
			grade.setGrade(Integer.parseInt(tableGrade.getGrade()));
		}
		if(tableGrade.getPrelegentLecturerName() != null && !"".equals(tableGrade.getPrelegentLecturerName())) {
			String[] splitedPrelegentLecturer = tableGrade.getPrelegentLecturerName().split(" ");
			if("p".equals(splitedPrelegentLecturer[0])) {
				grade.setLecturer(null);
				grade.setPrelegent(prelegentService.findPrelegentByNameAndSurname(splitedPrelegentLecturer[1], splitedPrelegentLecturer[2]));
			}
			else if("w".equals(splitedPrelegentLecturer[0])) {
				grade.setPrelegent(null);
				grade.setLecturer(lecturerService.findLecturerByNameAndSurname(splitedPrelegentLecturer[1], splitedPrelegentLecturer[2]));				
			}
		}
		return grade;
	}
}
