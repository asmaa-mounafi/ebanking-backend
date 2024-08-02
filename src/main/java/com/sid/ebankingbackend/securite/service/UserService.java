package com.sid.ebankingbackend.securite.service;

import com.sid.ebankingbackend.dtos.UserDTO;
import com.sid.ebankingbackend.securite.entitie.Role;
import com.sid.ebankingbackend.securite.entitie.User;

import java.util.List;

public interface UserService {

    User addNewUser(String username, String password , String email, String confirmPassword);
    Role addNewRole(String role);
    void addRoleToUser(String username,String role);
    void  removeRoleFromUser(String username,String role);
    User loadUserByUsername(String username);
    List<UserDTO> getUsers();
}
