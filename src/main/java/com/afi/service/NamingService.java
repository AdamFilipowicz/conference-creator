package com.afi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.dto.*;
import com.afi.mapper.*;
import com.afi.model.Conference;
import com.afi.tools.Pager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Service
public class NamingService {

	@Autowired
	private AbstractService abstractService;
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private ConferenceService conferenceService;
	@Autowired
	private EventService eventService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private PrelegentService prelegentService;
	@Autowired
	private LecturerService lecturerService;

	@Autowired
	private AbstractMapper abstractMapper;
	@Autowired
	private AlarmMapper alarmMapper;
	@Autowired
	private ConferenceMapper conferenceMapper;
	@Autowired
	private EventMapper eventMapper;
	@Autowired
	private GradeMapper gradeMapper;
	@Autowired
	private PrelegentMapper prelegentMapper;
	@Autowired
	private LecturerMapper lecturerMapper;

	public NamingService() {
	}

	public String saveObject(Object object, Conference conference) {
		if (object instanceof TableAbstract) {
			if (abstractService.updateAbstract(abstractMapper.toAbstract((TableAbstract) object), conference) == null) {
				return "Nieudany zapis abstraktu";
			}
		} else if (object instanceof TableAlarm) {
			if (alarmService.updateAlarm(alarmMapper.toAlarm((TableAlarm) object), conference) == null) {
				return "Nieudany zapis alarmu";
			}
		} else if (object instanceof TableConference) {
			if (conferenceService.updateConference(conferenceMapper.toConference((TableConference) object)) == null) {
				return "Istnieje już konferencja o podanej nazwie";
			}
		} else if (object instanceof TableEvent) {
			if (eventService.updateEvent(eventMapper.toEvent((TableEvent) object), conference) == null) {
				return "Nieudany zapis wydarzenia";
			}
		} else if (object instanceof TableGrade) {
			if (gradeService.updateGrade(gradeMapper.toGrade((TableGrade) object), conference) == null) {
				return "Nieudany zapis oceny";
			}
		} else if (object instanceof TablePrelegent) {
			if (prelegentService.updatePrelegent(prelegentMapper.toPrelegent((TablePrelegent) object),
					conference) == null) {
				return "Nieudany zapis prelegenta";
			}
		} else if (object instanceof TableLecturer) {
			if (lecturerService.updateLecturer(lecturerMapper.toLecturer((TableLecturer) object), conference) == null) {
				return "Nieudany zapis wykładowcy";
			}
		}
		return null;
	}

	public void deleteObject(Object object) {
		if (object instanceof TableAbstract) {
			abstractService.deleteAbstractById(((TableAbstract) object).getId());
		} else if (object instanceof TableAlarm) {
			alarmService.deleteAlarmById(((TableAlarm) object).getId());
		} else if (object instanceof TableConference) {
			conferenceService.deleteConferenceById(((TableConference) object).getId());
		} else if (object instanceof TableEvent) {
			eventService.deleteEventById(((TableEvent) object).getId());
		} else if (object instanceof TableGrade) {
			gradeService.deleteGradeById(((TableGrade) object).getId());
		} else if (object instanceof TablePrelegent) {
			prelegentService.deletePrelegentById(((TablePrelegent) object).getId());
		} else if (object instanceof TableLecturer) {
			lecturerService.deleteLecturerById(((TableLecturer) object).getId());
		}
	}

	public Pager findAll(Object object, Pager pager, Conference selectedConference, int tableLength, int buttonsToShow,
			Pageable pageable) {
		if (object instanceof TableAbstract) {
			Page<TableAbstract> tablePage = abstractService.findAllByConference(selectedConference, pageable)
					.map(abstractMapper::toTableAbstract);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent())
					: new ArrayList<Object>();
			if (tableLength > list.size()) {
				list.add(new TableAbstract());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			// jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym
			// dokumentem
			if (tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			} else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		} else if (object instanceof TableAlarm) {
			Page<TableAlarm> tablePage = alarmService.findAllByConference(selectedConference, pageable)
					.map(alarmMapper::toTableAlarm);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent())
					: new ArrayList<Object>();
			if (tableLength > list.size()) {
				list.add(new TableAlarm());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			// jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym
			// dokumentem
			if (tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			} else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		} else if (object instanceof TableConference) {
			Page<TableConference> tablePage = conferenceService.findAll(pageable)
					.map(conferenceMapper::toTableConference);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent())
					: new ArrayList<Object>();
			if (tableLength > list.size()) {
				list.add(new TableConference());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			// jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym
			// dokumentem
			if (tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			} else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		} else if (object instanceof TableEvent) {
			Page<TableEvent> tablePage = eventService.findAllByConference(selectedConference, pageable)
					.map(eventMapper::toTableEvent);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent())
					: new ArrayList<Object>();
			if (tableLength > list.size()) {
				list.add(new TableEvent());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			// jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym
			// dokumentem
			if (tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			} else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		} else if (object instanceof TableGrade) {
			Page<TableGrade> tablePage = gradeService.findAllByConference(selectedConference, pageable)
					.map(gradeMapper::toTableGrade);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent())
					: new ArrayList<Object>();
			if (tableLength > list.size()) {
				list.add(new TableGrade());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			// jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym
			// dokumentem
			if (tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			} else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		} else if (object instanceof TablePrelegent) {
			Page<TablePrelegent> tablePage = prelegentService.findAllByConference(selectedConference, pageable)
					.map(prelegentMapper::toTablePrelegent);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent())
					: new ArrayList<Object>();
			if (tableLength > list.size()) {
				list.add(new TablePrelegent());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			// jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym
			// dokumentem
			if (tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			} else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		} else if (object instanceof TableLecturer) {
			Page<TableLecturer> tablePage = lecturerService.findAllByConference(selectedConference, pageable)
					.map(lecturerMapper::toTableLecturer);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent())
					: new ArrayList<Object>();
			if (tableLength > list.size()) {
				list.add(new TableLecturer());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			// jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym
			// dokumentem
			if (tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			} else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		}
		return null;
	}

	public ObservableList<String> findEventNames(Conference conference) {
		return FXCollections.observableArrayList(eventService.findAllByConference(conference).stream()
				.map(eventMapper::toStringList).collect(Collectors.toList()));
	}

	public ObservableList<String> findLecturerPrelegentNames(Conference conference) {
		List<String> prelegents = prelegentService.findAllByConference(conference).stream().map(prelegentMapper::toStringList)
				.collect(Collectors.toList());
		List<String> lecturers = lecturerService.findAllByConference(conference).stream()
				.map(lecturerMapper::toStringList).collect(Collectors.toList());
		prelegents.addAll(lecturers);
		return FXCollections.observableArrayList(prelegents);
	}

}
