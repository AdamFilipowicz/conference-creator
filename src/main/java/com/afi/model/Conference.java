package com.afi.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	@Column(name = "conference_start")
	private Timestamp conferenceStart;
	
	@NotNull
	@Column(name = "conference_end")
	private Timestamp conferenceEnd;
	
	@OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	private List<Prelegent> prelegents = new ArrayList<>();
	
	@OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	private List<Prelegent> lecturers = new ArrayList<>();
	
	@OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	private List<Abstract> abstracts = new ArrayList<>();
	
	@OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	private List<Alarm> alarms = new ArrayList<>();
	
	@OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	private List<Event> events = new ArrayList<>();
	
	@OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	private List<Grade> grades = new ArrayList<>();
	
	@OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.REFRESH })
	private List<Config> configs = new ArrayList<>();

	public Conference() {}

	public Conference(@NotNull @NotBlank String name) {
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

	public Timestamp getConferenceStart() {
		return conferenceStart;
	}

	public void setConferenceStart(Timestamp conferenceStart) {
		this.conferenceStart = conferenceStart;
	}

	public Timestamp getConferenceEnd() {
		return conferenceEnd;
	}

	public void setConferenceEnd(Timestamp conferenceEnd) {
		this.conferenceEnd = conferenceEnd;
	}

	public List<Prelegent> getPrelegents() {
		return prelegents;
	}

	public void clearPrelegents() {
		prelegents.clear();
	}
	
	public void addPrelegent(Prelegent prelegent) {
		prelegents.add(prelegent);
		prelegent.setConference(this);
	}

	public void removePrelegent(Prelegent prelegent) {
		prelegents.remove(prelegent);
		prelegent.setConference(null);
	}
}
