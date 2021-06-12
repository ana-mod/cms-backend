package com.example.cms.repositories;

import com.example.cms.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    public boolean existsByUserId(Long userId);

    public Optional<Author> findByUserId(@Param("userId") Long userId);
}
