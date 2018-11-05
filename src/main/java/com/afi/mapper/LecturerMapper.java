package com.afi.mapper;

import org.springframework.stereotype.Component;

import com.afi.dto.TableLecturer;
import com.afi.model.Lecturer;

@Component
public class LecturerMapper {

	public TableLecturer toTableLecturer(Lecturer lecturer) {
		TableLecturer tableLecturer = new TableLecturer();
		tableLecturer.setId(lecturer.getId());
		tableLecturer.setName(lecturer.getName());
		tableLecturer.setSurname(lecturer.getSurname());
		if(lecturer.getAge() != 0) {
			tableLecturer.setAge(Integer.toString(lecturer.getAge()));
		}
		else {
			tableLecturer.setAge(null);
		}
		return tableLecturer;
	}
	
	public Lecturer toLecturer(TableLecturer tableLecturer) {
		Lecturer lecturer = new Lecturer();
		lecturer.setId(tableLecturer.getId());
		lecturer.setName(tableLecturer.getName());
		lecturer.setSurname(tableLecturer.getSurname());
		if(tableLecturer.getAge() != null && !"".equals(tableLecturer.getAge())) {
			lecturer.setAge(Integer.parseInt(tableLecturer.getAge()));
		}
		return lecturer;
	}
	
	public String toStringList(Lecturer lecturer) {
		return "w "+lecturer.getName()+" "+lecturer.getSurname();
	}
}
