package com.example.cms.repositories;

import com.example.cms.models.Conference;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConferenceRepository extends CrudRepository<Conference, Long> {

    public List<Conference> findAllByCreatorId(long id);

    @Query(nativeQuery = true, value = "SELECT conference_id " +
    "FROM cms_conference_users " +
    "WHERE user_id=:userId")
    List<Long> findEnrolledByUserId(@Param("userId") Long userId);
    @Query("select c from Conference c where search like %?1%")
    List<Conference> findByName(String search, Pageable pageable);

}
