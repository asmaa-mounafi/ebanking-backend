package com.sid.ebankingbackend.securite;


import com.sid.ebankingbackend.dtos.UserDTO;
import com.sid.ebankingbackend.mappers.BankAccountMapperImpl;
import com.sid.ebankingbackend.securite.Response.UserResponse;
import com.sid.ebankingbackend.securite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class SecurityController {
    @Autowired
    private AuthenticationManager authenticationManager;
   @Autowired
    private JwtEncoder jwtEncoder;
    private UserService userService;
    private BankAccountMapperImpl mapper;
    @GetMapping("/profile")
    public Authentication authentication(Authentication authentication){
        return  authentication;
    }
@PostMapping("/login")
    public Map<String,String> login(String username,String password){
org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

    Instant instant=Instant.now();
   String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
    JwtClaimsSet jwtClaimsSet= JwtClaimsSet.builder()
            .issuedAt(instant)
            .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
            .subject(username)
            .claim("scope",scope)
            .build();
    JwtEncoderParameters jwtEncoderParameters=
            JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );
String  jwt=jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    return Map.of("access_token",jwt);
    }
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<UserResponse> getUsers(){
        List<UserDTO> UserDTOS = userService.getUsers();
        List<UserResponse> userResponses = UserDTOS.stream()
                .map(userDTO -> mapper.fromAppUserDTOResponse(userDTO))
                .toList();
        return userResponses;
    }
}
