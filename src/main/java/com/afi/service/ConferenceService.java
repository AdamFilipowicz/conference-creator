package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.repository.ConferenceRepository;

@Service
public class ConferenceService {
	
	@Autowired
	private ConferenceRepository conferenceRepository;

    public ConferenceService() {}
    
    public Page<Conference> findAll(Pageable pageable){
    	return conferenceRepository.findAll(pageable);
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
