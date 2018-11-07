package com.afi.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afi.dto.TableAbstract;
import com.afi.model.Abstract;
import com.afi.service.LecturerService;
import com.afi.service.PrelegentService;

@Component
public class AbstractMapper {
	
	@Autowired
	private PrelegentService prelegentService;
	@Autowired
	private LecturerService lecturerService;

	public TableAbstract toTableAbstract(Abstract abstractt) {
		TableAbstract tableAbstract = new TableAbstract();
		tableAbstract.setId(abstractt.getId());
		tableAbstract.setName(abstractt.getName());
		tableAbstract.setPdfPath(abstractt.getPdfPath());
		if(abstractt.getLecturer() != null) {
			tableAbstract.setPrelegentLecturerName("w "+abstractt.getLecturer().getName()+" "+abstractt.getLecturer().getSurname());
		}
		else if(abstractt.getPrelegent() != null) {
			tableAbstract.setPrelegentLecturerName("p "+abstractt.getPrelegent().getName()+" "+abstractt.getPrelegent().getSurname());
		}
		return tableAbstract;
	}
	
	public Abstract toAbstract(TableAbstract tableAbstract) {
		Abstract abstractt = new Abstract();
		abstractt.setId(tableAbstract.getId());
		abstractt.setName(tableAbstract.getName());
		abstractt.setPdfPath(tableAbstract.getPdfPath());
		if(tableAbstract.getPrelegentLecturerName() != null && !"".equals(tableAbstract.getPrelegentLecturerName())) {
			String[] splitedPrelegentLecturer = tableAbstract.getPrelegentLecturerName().split(" ");
			if("p".equals(splitedPrelegentLecturer[0])) {
				abstractt.setLecturer(null);
				abstractt.setPrelegent(prelegentService.findPrelegentByNameAndSurname(splitedPrelegentLecturer[1], splitedPrelegentLecturer[2]));
			}
			else if("w".equals(splitedPrelegentLecturer[0])) {
				abstractt.setPrelegent(null);
				abstractt.setLecturer(lecturerService.findLecturerByNameAndSurname(splitedPrelegentLecturer[1], splitedPrelegentLecturer[2]));				
			}
		}
		return abstractt;
	}
}
