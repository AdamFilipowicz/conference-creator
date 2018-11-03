package com.afi.mapper;

import org.springframework.stereotype.Component;

import com.afi.dto.TableGrade;
import com.afi.model.Grade;

@Component
public class GradeMapper {

	public TableGrade toTableGrade(Grade grade) {
		TableGrade tableGrade = new TableGrade();
		tableGrade.setId(grade.getId());
		if(grade.getGrade() != 0) {
			tableGrade.setGrade(Integer.toString(grade.getGrade()));
		}
		else {
			tableGrade.setGrade(null);
		}
		return tableGrade;
	}
	
	public Grade toGrade(TableGrade tableGrade) {
		Grade grade = new Grade();
		grade.setId(tableGrade.getId());
		if(tableGrade.getGrade() != null && !"".equals(tableGrade.getGrade())) {
			grade.setGrade(Integer.parseInt(tableGrade.getGrade()));
		}
		return grade;
	}
}
