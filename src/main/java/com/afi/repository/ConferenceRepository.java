package com.afi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.afi.model.Conference;

import java.util.List;

@Repository
public interface ConferenceRepository extends CrudRepository<Conference, Long>, PagingAndSortingRepository<Conference, Long>{
    long count();

    List<Conference> findAll();

    Page<Conference> findAllByOrderById(Pageable pageable);
    
    Conference findByName(String name);
}
