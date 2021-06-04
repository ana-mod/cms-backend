package com.example.cms.repositories;

import com.example.cms.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    public boolean existsByUserId(Long userId);

//    @Query(nativeQuery = true, value = "SELECT * FROM cms_author JOIN cms_user WHERE UserId=:userId LIMIT 1")
    public Author findByUserId(@Param("userId") Long userId);
}
