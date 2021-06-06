package com.example.cms.repositories;

import com.example.cms.models.Conference;

import antlr.collections.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConferenceRepository extends CrudRepository<Conference, Long> {
	@Query("select s from Conference where topic like %?1%")
	List<Conference> findByName(String search, Pageable pageable);
}