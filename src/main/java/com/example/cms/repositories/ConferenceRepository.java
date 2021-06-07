package com.example.cms.repositories;

import com.example.cms.models.Conference;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConferenceRepository extends CrudRepository<Conference, Long> {

    public List<Conference> findAllByAuthorId(long id);
}
