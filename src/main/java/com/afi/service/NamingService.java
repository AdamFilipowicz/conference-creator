package com.afi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afi.dto.TableConference;
import com.afi.mapper.ConferenceMapper;
import com.afi.model.Prelegent;

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

	public List<Object> findAll(Object object, int tableLength) {
		if (object instanceof TableConference) {
			List<TableConference> tableList = conferenceService.findAll().stream().map(conferenceMapper::toTableConference)
					.collect(Collectors.toList());
			List<Object> list = !tableList.isEmpty() ? new ArrayList<Object>(tableList) : new ArrayList<Object>();
			if(tableLength > list.size()) {
				list.add(new TableConference());
			}
			return list;
		} 
		else if (object instanceof Prelegent) {
			List<Prelegent> tableList = prelegentService.findAll();
			List<Object> list = !tableList.isEmpty() ? new ArrayList<Object>(tableList) : new ArrayList<Object>();
			if(tableLength > list.size()) {
				list.add(new Prelegent());
			}
			return list;
		}
		return null;
	}

}
