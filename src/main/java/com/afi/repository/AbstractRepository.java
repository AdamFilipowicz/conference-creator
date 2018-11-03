package com.afi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

import com.afi.model.Conference;
import com.afi.model.Abstract;

import java.util.List;

@Repository
public interface AbstractRepository extends JpaRepository<Abstract, Long>, PagingAndSortingRepository<Abstract, Long> {
    long count();

    List<Abstract> findAll();
    
    Page<Abstract> findAllByConferenceOrderById(Conference conference, Pageable pageable);
    
    Abstract findByName(String name);
}
