package com.afi.mapper;

import org.springframework.stereotype.Component;

import com.afi.dto.TablePrelegent;
import com.afi.model.Prelegent;

@Component
public class PrelegentMapper {

	public TablePrelegent toTablePrelegent(Prelegent prelegent) {
		TablePrelegent tablePrelegent = new TablePrelegent();
		tablePrelegent.setId(prelegent.getId());
		tablePrelegent.setName(prelegent.getName());
		tablePrelegent.setSurname(prelegent.getSurname());
		if(prelegent.getAge() != 0) {
			tablePrelegent.setAge(Integer.toString(prelegent.getAge()));
		}
		else {
			tablePrelegent.setAge(null);
		}
		return tablePrelegent;
	}
	
	public Prelegent toPrelegent(TablePrelegent tablePrelegent) {
		Prelegent prelegent = new Prelegent();
		prelegent.setId(tablePrelegent.getId());
		prelegent.setName(tablePrelegent.getName());
		prelegent.setSurname(tablePrelegent.getSurname());
		if(tablePrelegent.getAge() != null && !"".equals(tablePrelegent.getAge())) {
			prelegent.setAge(Integer.parseInt(tablePrelegent.getAge()));
		}
		return prelegent;
	}
}
