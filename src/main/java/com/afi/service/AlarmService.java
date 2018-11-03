package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.model.Alarm;
import com.afi.repository.AlarmRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class AlarmService {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	private AlarmRepository alarmRepository;

	@Autowired
    public AlarmService(EntityManager entityManager, AlarmRepository alarmRepository) {
		this.entityManager = entityManager;
		this.alarmRepository = alarmRepository;
	}
    
    public Page<Alarm> findAllByConference(Conference conference, Pageable pageable){
    	return alarmRepository.findAllByConferenceOrderById(conference, pageable);
    }

    public Alarm findAlarmByName(String name) {
        return alarmRepository.findByName(name);
    }
    
    public Alarm updateAlarm(Alarm alarm, Conference conference) {
    	alarm.setConference(conference);
    	Alarm saved = alarmRepository.save(alarm);
        return saved;
    }
    
    public void deleteAlarmById(long id) {
        alarmRepository.delete(alarmRepository.findById(id).orElse(null));
    }
}
