package com.afi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afi.model.Prelegent;
import com.afi.repository.PrelegentRepository;

@Service
public class PrelegentService {
	@Autowired
	private PrelegentRepository prelegentRepository;

    public PrelegentService() {}
    
    public List<Prelegent> findAll(){
    	List<Prelegent> prelList = prelegentRepository.findAll();
    	if(prelList.isEmpty()) {
    		return new ArrayList<Prelegent>();
    	}
    	return prelList;
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
