package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.model.Conference;
import com.afi.model.Lecturer;
import com.afi.repository.LecturerRepository;

import java.util.List;

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
    
    public List<Lecturer> findAllByConference(Conference conference){
    	return lecturerRepository.findAllByConferenceOrderById(conference);
    }

    public Lecturer findLecturerByNameAndSurname(String name, String surname) {
        return lecturerRepository.findByNameAndSurname(name, surname);
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
