package com.afi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

import com.afi.model.Conference;
import com.afi.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, PagingAndSortingRepository<Event, Long> {
    long count();

    List<Event> findAll();
    
    Page<Event> findAllByConferenceOrderById(Conference conference, Pageable pageable);
    
    List<Event> findAllByConferenceOrderById(Conference conference);
    
    Event findByName(String name);
}
