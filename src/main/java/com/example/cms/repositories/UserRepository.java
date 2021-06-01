package com.example.cms.repositories;

import com.example.cms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM USER " +
            "WHERE LOGIN=:loginOrEmail OR EMAIL=:loginOrEmail")
    User authenticateByLoginOrEmail(@Param("loginOrEmail") String loginOrEmail);

}
