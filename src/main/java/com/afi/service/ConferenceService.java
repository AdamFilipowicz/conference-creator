package com.afi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.repository.ConferenceRepository;

@Service
public class ConferenceService {
	
	@Autowired
	private ConferenceRepository conferenceRepository;

    public ConferenceService() {}
    
    public List<Conference> findAll(){
    	List<Conference> confList = conferenceRepository.findAll();
    	if(confList.isEmpty()) {
    		return new ArrayList<Conference>();
    	}
    	return confList;
    }

    public Conference findConferenceByName(String name) {
        return conferenceRepository.findByName(name);
    }
    
    public Conference updateConference(Conference conference) {
        return conferenceRepository.save(conference);
    }
    
    public void deleteConferenceById(long id) {
        conferenceRepository.delete(conferenceRepository.findById(id).orElse(null));
    }
}
