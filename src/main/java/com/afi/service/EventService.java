package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.model.Event;
import com.afi.repository.EventRepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class EventService {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	private EventRepository eventRepository;

	@Autowired
    public EventService(EntityManager entityManager, EventRepository eventRepository) {
		this.entityManager = entityManager;
		this.eventRepository = eventRepository;
	}
    
    public Page<Event> findAllByConference(Conference conference, Pageable pageable){
    	return eventRepository.findAllByConferenceOrderById(conference, pageable);
    }
    
    public List<Event> findAllByConference(Conference conference){
    	return eventRepository.findAllByConferenceOrderById(conference);
    }

    public Event findEventByName(String name) {
        return eventRepository.findByName(name);
    }
    
    public Event updateEvent(Event event, Conference conference) {
    	event.setConference(conference);
    	Event saved = eventRepository.save(event);
        return saved;
    }
    
    public void deleteEventById(long id) {
        eventRepository.delete(eventRepository.findById(id).orElse(null));
    }
}
