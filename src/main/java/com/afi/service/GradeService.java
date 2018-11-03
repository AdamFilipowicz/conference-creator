package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.model.Grade;
import com.afi.repository.GradeRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class GradeService {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	private GradeRepository gradeRepository;

	@Autowired
    public GradeService(EntityManager entityManager, GradeRepository gradeRepository) {
		this.entityManager = entityManager;
		this.gradeRepository = gradeRepository;
	}
    
    public Page<Grade> findAllByConference(Conference conference, Pageable pageable){
    	return gradeRepository.findAllByConferenceOrderById(conference, pageable);
    }
    
    public Grade updateGrade(Grade grade, Conference conference) {
    	grade.setConference(conference);
    	Grade saved = gradeRepository.save(grade);
        return saved;
    }
    
    public void deleteGradeById(long id) {
        gradeRepository.delete(gradeRepository.findById(id).orElse(null));
    }
}
