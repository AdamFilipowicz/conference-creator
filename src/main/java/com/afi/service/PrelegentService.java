package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.model.Prelegent;
import com.afi.repository.PrelegentRepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class PrelegentService {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	private PrelegentRepository prelegentRepository;

	@Autowired
    public PrelegentService(EntityManager entityManager, PrelegentRepository prelegentRepository) {
		this.entityManager = entityManager;
		this.prelegentRepository = prelegentRepository;
	}
    
    public Page<Prelegent> findAllByConference(Conference conference, Pageable pageable){
    	return prelegentRepository.findAllByConferenceOrderById(conference, pageable);
    }
    
    public List<Prelegent> findAllByConference(Conference conference){
    	return prelegentRepository.findAllByConferenceOrderById(conference);
    }

    public Prelegent findPrelegentByNameAndSurname(String name, String surname) {
        return prelegentRepository.findByNameAndSurname(name, surname);
    }
    
    public Prelegent updatePrelegent(Prelegent prelegent, Conference conference) {
    	prelegent.setConference(conference);
    	Prelegent saved = prelegentRepository.save(prelegent);
        return saved;
    }
    
    public void deletePrelegentById(long id) {
        prelegentRepository.delete(prelegentRepository.findById(id).orElse(null));
    }
}
