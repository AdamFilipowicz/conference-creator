package com.afi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afi.model.Prelegent;
import com.afi.repository.PrelegentRepository;

@Service
public class PrelegentService {
	@Autowired
	private PrelegentRepository prelegentRepository;

    public PrelegentService() {}
    
    public Page<Prelegent> findAll(Pageable pageable){
    	return prelegentRepository.findAll(pageable);
    }

    public Prelegent findPrelegentByName(String name) {
        return prelegentRepository.findByName(name);
    }
    
    public Prelegent updatePrelegent(Prelegent prelegent) {
        return prelegentRepository.save(prelegent);
    }
    
    public void deletePrelegentById(long id) {
        prelegentRepository.delete(prelegentRepository.findById(id).orElse(null));
    }
}
