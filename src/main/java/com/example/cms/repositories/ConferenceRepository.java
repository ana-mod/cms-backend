package com.example.cms.repositories;

import com.example.cms.models.Conference;

import org.springframework.data.repository.CrudRepository;

public interface ConferenceRepository extends CrudRepository<Conference, Long> {}