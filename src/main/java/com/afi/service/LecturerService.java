package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.model.Lecturer;
import com.afi.repository.LecturerRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class LecturerService {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	private LecturerRepository lecturerRepository;

	@Autowired
    public LecturerService(EntityManager entityManager, LecturerRepository lecturerRepository) {
		this.entityManager = entityManager;
		this.lecturerRepository = lecturerRepository;
	}
    
    public Page<Lecturer> findAllByConference(Conference conference, Pageable pageable){
    	return lecturerRepository.findAllByConferenceOrderById(conference, pageable);
    }

    public Lecturer findLecturerByName(String name) {
        return lecturerRepository.findByName(name);
    }
    
    public Lecturer updateLecturer(Lecturer lecturer, Conference conference) {
    	lecturer.setConference(conference);
    	Lecturer saved = lecturerRepository.save(lecturer);
        return saved;
    }
    
    public void deleteLecturerById(long id) {
        lecturerRepository.delete(lecturerRepository.findById(id).orElse(null));
    }
}
