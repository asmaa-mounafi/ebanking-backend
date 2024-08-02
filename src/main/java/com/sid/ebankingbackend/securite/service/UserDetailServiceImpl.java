package com.sid.ebankingbackend.securite.service;

import com.sid.ebankingbackend.securite.entitie.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
   private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.loadUserByUsername(username);
        if (user==null) throw new UsernameNotFoundException(String.format("User %s not found",username));

        String[] roles = user.getRoles().stream().map(u -> u.getRole()).toArray(String[]::new);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .roles(roles).build();
        return userDetails;
    }
}
