package com.afi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.dto.TableConference;
import com.afi.mapper.ConferenceMapper;
import com.afi.model.Prelegent;
import com.afi.tools.Pager;

@Service
public class NamingService {

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private PrelegentService prelegentService;

	@Autowired
	private ConferenceMapper conferenceMapper;

	public NamingService() {
	}

	public Object saveObject(Object object) {
		if (object instanceof TableConference) {
			return conferenceService.updateConference(conferenceMapper.toConference((TableConference) object));
		} 
		else if (object instanceof Prelegent) {
			return prelegentService.updatePrelegent((Prelegent) object);
		}
		return null;
	}

	public void deleteObject(Object object) {
		if (object instanceof TableConference) {
			conferenceService.deleteConferenceById(((TableConference) object).getId());
		} 
		else if (object instanceof Prelegent) {
			prelegentService.deletePrelegentById(((Prelegent) object).getId());
		}
	}

	public Pager findAll(Object object, Pager pager, int tableLength, int buttonsToShow, Pageable pageable) {
		if (object instanceof TableConference) {
			Page<TableConference> tablePage = conferenceService.findAll(pageable).map(conferenceMapper::toTableConference);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent()) : new ArrayList<Object>();
			if(tableLength > list.size()) {
				list.add(new TableConference());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			pager.setTotalPages(tablePage.getTotalPages());
			return pager;
		}
		else if (object instanceof Prelegent) {
			Page<Prelegent> tablePage = prelegentService.findAll(pageable);
			List<Object> list = !tablePage.getContent().isEmpty() ? new ArrayList<Object>(tablePage.getContent()) : new ArrayList<Object>();
			if(tableLength > list.size()) {
				list.add(new Prelegent());
			}
			pager = new Pager(list, tablePage.getTotalPages(), tablePage.getNumber(), buttonsToShow);
			pager.setTotalPages(tablePage.getTotalPages());
			return pager;
		}
		return null;
	}

}
