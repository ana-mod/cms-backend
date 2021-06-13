package com.example.cms.repositories;

import com.example.cms.models.Presentation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PresentationRepository extends CrudRepository<Presentation, Long> {
    
}
