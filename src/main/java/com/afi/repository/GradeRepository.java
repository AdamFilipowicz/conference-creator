package com.afi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

import com.afi.model.Conference;
import com.afi.model.Grade;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>, PagingAndSortingRepository<Grade, Long> {
    long count();

    List<Grade> findAll();
    
    Page<Grade> findAllByConferenceOrderById(Conference conference, Pageable pageable);
}
