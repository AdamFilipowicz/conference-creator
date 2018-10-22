package com.afi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

import com.afi.model.Conference;
import com.afi.model.Lecturer;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long>, PagingAndSortingRepository<Lecturer, Long> {
    long count();

    List<Lecturer> findAll();
    
    Page<Lecturer> findAllByConferenceOrderById(Conference conference, Pageable pageable);
    
    Lecturer findByName(String name);
}
