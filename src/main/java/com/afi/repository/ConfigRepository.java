package com.afi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

import com.afi.model.Conference;
import com.afi.model.Config;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long>, PagingAndSortingRepository<Config, Long> {
    long count();

    List<Config> findAll();
    
    Config findByKeyAndConference(String key, Conference conference);
}
