package com.example.cms.repositories;

import com.example.cms.models.Conference;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ConferenceRepository extends CrudRepository<Conference, Long> {
    @Query("select c from Conference c where search like %?1%")
    List<Conference> findByName(String search, Pageable pageable);
}
