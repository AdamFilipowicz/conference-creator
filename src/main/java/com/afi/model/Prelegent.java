package com.afi.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "conf_prelegent")
public class Prelegent {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_prelegent", updatable = false, nullable = false)
	private long id;

	@NotNull
	@NotBlank
	private String name;
	
	@NotNull
	@NotBlank
	private String surname;
	
	private int age;
	
	@NotNull
	@Column(name = "average_grade")
	private float averageGrade;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "conference_id")
	private Conference conference;
	
	@OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	private List<Abstract> abstracts = new ArrayList<>();

	public Prelegent() {}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Prelegent(@NotNull @NotBlank String name, @NotNull @NotBlank String surname, int age,
			@NotNull float averageGrade) {
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.averageGrade = averageGrade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public float getAverageGrade() {
		return averageGrade;
	}

	public void setAverageGrade(float averageGrade) {
		this.averageGrade = averageGrade;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}
}
