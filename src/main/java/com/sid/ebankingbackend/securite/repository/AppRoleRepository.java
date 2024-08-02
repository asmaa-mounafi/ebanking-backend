package com.sid.ebankingbackend.securite.repository;

import com.sid.ebankingbackend.securite.entitie.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<Role,String> {

}
