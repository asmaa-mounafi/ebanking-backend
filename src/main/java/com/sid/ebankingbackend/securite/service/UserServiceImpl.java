package com.sid.ebankingbackend.securite.service;

import com.sid.ebankingbackend.dtos.UserDTO;
import com.sid.ebankingbackend.mappers.BankAccountMapperImpl;
import com.sid.ebankingbackend.securite.entitie.Role;
import com.sid.ebankingbackend.securite.entitie.User;
import com.sid.ebankingbackend.securite.repository.AppRoleRepository;
import com.sid.ebankingbackend.securite.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;
    private BankAccountMapperImpl mapper;

    @Override
    public User addNewUser(String username, String password, String email, String confirmPassword) {
        User user=appUserRepository.findByUsername(username);
        if (user!=null)throw  new RuntimeException("this user already exist");
        if (!password.equals(confirmPassword)) throw  new RuntimeException("Password not match");
        user = User.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        User savedUser = appUserRepository.save(user);
        return savedUser;
    }

    @Override
    public Role addNewRole(String role) {
        Role role1 = appRoleRepository.findById(role).orElse(null);
        if (role1!=null) throw new RuntimeException("This role already exist");
        role1=Role.builder()
                .role(role)
                .build();
        return appRoleRepository.save(role1);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        User user = appUserRepository.findByUsername(username);
        Role role1 = appRoleRepository.findById(role).get();
        user.getRoles().add(role1);
        //appUserRepository.save(user);

    }

    @Override
    public void removeRoleFromUser(String username, String role) {
        User user = appUserRepository.findByUsername(username);
        Role role1 = appRoleRepository.findById(role).get();
        user.getRoles().remove(role1);
    }

    @Override
    public User loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
    @Override
    public List<UserDTO> getUsers() {
        List<User> appUsers = appUserRepository.findAll();
        List<UserDTO> appUserDTOS = appUsers.stream()
                .map(appUser -> mapper.fromAppUser(appUser))
                .toList();
        return appUserDTOS;
    }
}
