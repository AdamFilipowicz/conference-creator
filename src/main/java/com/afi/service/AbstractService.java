package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.model.Abstract;
import com.afi.repository.AbstractRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class AbstractService {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	private AbstractRepository abstractRepository;

	@Autowired
    public AbstractService(EntityManager entityManager, AbstractRepository abstractRepository) {
		this.entityManager = entityManager;
		this.abstractRepository = abstractRepository;
	}
    
    public Page<Abstract> findAllByConference(Conference conference, Pageable pageable){
    	return abstractRepository.findAllByConferenceOrderById(conference, pageable);
    }

    public Abstract findAbstractByName(String name) {
        return abstractRepository.findByName(name);
    }
    
    public Abstract updateAbstract(Abstract abstractt, Conference conference) {
    	abstractt.setConference(conference);
    	Abstract saved = abstractRepository.save(abstractt);
        return saved;
    }
    
    public void deleteAbstractById(long id) {
        abstractRepository.delete(abstractRepository.findById(id).orElse(null));
    }
}
