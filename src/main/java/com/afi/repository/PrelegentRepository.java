package com.afi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

import com.afi.model.Conference;
import com.afi.model.Prelegent;

import java.util.List;

@Repository
public interface PrelegentRepository extends JpaRepository<Prelegent, Long>, PagingAndSortingRepository<Prelegent, Long> {
    long count();

    List<Prelegent> findAll();
    
    Page<Prelegent> findAllByConferenceOrderById(Conference conference, Pageable pageable);
    
    List<Prelegent> findAllByConferenceOrderById(Conference conference);
    
    Prelegent findByNameAndSurname(String name, String surname);
}
