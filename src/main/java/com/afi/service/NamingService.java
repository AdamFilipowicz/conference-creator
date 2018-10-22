package com.afi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.dto.TableConference;
import com.afi.dto.TableLecturer;
import com.afi.dto.TablePrelegent;
import com.afi.mapper.ConferenceMapper;
import com.afi.mapper.LecturerMapper;
import com.afi.mapper.PrelegentMapper;
import com.afi.model.Conference;
import com.afi.tools.Pager;

@Service
public class NamingService {

	@Autowired
	private ConferenceService conferenceService;
	@Autowired
	private PrelegentService prelegentService;
	@Autowired
	private LecturerService lecturerService;

	@Autowired
	private ConferenceMapper conferenceMapper;
	@Autowired
	private PrelegentMapper prelegentMapper;
	@Autowired
	private LecturerMapper lecturerMapper;

	public NamingService() {
	}

	public String saveObject(Object object, Conference conference) {
		if (object instanceof TableConference) {
			if(conferenceService.updateConference(conferenceMapper.toConference((TableConference) object)) == null) {
				return "Istnieje już konferencja o podanej nazwie";
			}
		} 
		else if (object instanceof TablePrelegent) {
			if(prelegentService.updatePrelegent(prelegentMapper.toPrelegent((TablePrelegent) object), conference) == null) {
				return "Nieudany zapis prelegenta";
			}
		}
		else if (object instanceof TableLecturer) {
			if(lecturerService.updateLecturer(lecturerMapper.toLecturer((TableLecturer) object), conference) == null) {
				return "Nieudany zapis wykładowcy";
			}
		}
		return null;
	}

	public void deleteObject(Object object) {
		if (object instanceof TableConference) {
			conferenceService.deleteConferenceById(((TableConference) object).getId());
		} 
		else if (object instanceof TablePrelegent) {
			prelegentService.deletePrelegentById(((TablePrelegent) object).getId());
		}
		else if (object instanceof TableLecturer) {
			lecturerService.deleteLecturerById(((TableLecturer) object).getId());
		}
	}

	public Pager findAll(Object object, Pager pager, Conference selectedConference, int tableLength, int buttonsToShow, Pageable pageable) {
		if (object instanceof TableConference) {
			Page<TableConference> tablePage = conferenceService.findAll(pageable).map(conferenceMapper::toTableConference);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent()) : new ArrayList<Object>();
			if(tableLength > list.size()) {
				list.add(new TableConference());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			//jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym dokumentem
			if(tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			}
			else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		}
		else if (object instanceof TablePrelegent) {
			Page<TablePrelegent> tablePage = prelegentService.findAllByConference(selectedConference, pageable).map(prelegentMapper::toTablePrelegent);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent()) : new ArrayList<Object>();
			if(tableLength > list.size()) {
				list.add(new TablePrelegent());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			//jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym dokumentem
			if(tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			}
			else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		}
		else if (object instanceof TableLecturer) {
			Page<TableLecturer> tablePage = lecturerService.findAllByConference(selectedConference, pageable).map(lecturerMapper::toTableLecturer);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent()) : new ArrayList<Object>();
			if(tableLength > list.size()) {
				list.add(new TableLecturer());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			//jesli jest juz wypelniona ostatnia strona to tworzymy nowa z pustym dokumentem
			if(tablePage.getTotalElements() % tableLength == 0 && tablePage.getTotalElements() >= tableLength) {
				pager.setTotalPages(tablePage.getTotalPages() + 1);
				pager.setEndPage(pager.getEndPage() + 1);
			}
			else {
				pager.setTotalPages(tablePage.getTotalPages());
			}
			return pager;
		}
		return null;
	}

}
