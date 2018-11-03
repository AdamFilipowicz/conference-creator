package com.afi.mapper;

import org.springframework.stereotype.Component;

import com.afi.dto.TableAbstract;
import com.afi.model.Abstract;

@Component
public class AbstractMapper {

	public TableAbstract toTableAbstract(Abstract abstractt) {
		TableAbstract tableAbstract = new TableAbstract();
		tableAbstract.setId(abstractt.getId());
		tableAbstract.setName(abstractt.getName());
		tableAbstract.setPdfPath(abstractt.getPdfPath());
		return tableAbstract;
	}
	
	public Abstract toAbstract(TableAbstract tableAbstract) {
		Abstract abstractt = new Abstract();
		abstractt.setId(tableAbstract.getId());
		abstractt.setName(tableAbstract.getName());
		abstractt.setPdfPath(tableAbstract.getPdfPath());
		return abstractt;
	}
}
