package com.afi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.afi.model.Conference;
import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long>{
    long count();

    List<Conference> findAll();

    Page<Conference> findAll(Pageable pageable);
    
    Conference findByName(String name);
}
