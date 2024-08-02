package com.sid.ebankingbackend.securite.repository;

import com.sid.ebankingbackend.securite.entitie.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<User,String> {

    User findByUsername(String username);
}
