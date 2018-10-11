package com.afi.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "conf_conference")
public class Conference {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_conference", updatable = false, nullable = false)
	private long id;

	@NotNull
	@NotBlank
	@Column(unique = true)
	private String name;
	
	@NotNull
	@Column(name = "start_date")
	private Timestamp startDate;

	public Conference() {}
	
	public Conference(@NotNull String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
}
