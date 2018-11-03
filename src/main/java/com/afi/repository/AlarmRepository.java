package com.afi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

import com.afi.model.Conference;
import com.afi.model.Alarm;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>, PagingAndSortingRepository<Alarm, Long> {
    long count();

    List<Alarm> findAll();
    
    Page<Alarm> findAllByConferenceOrderById(Conference conference, Pageable pageable);
    
    Alarm findByName(String name);
}
