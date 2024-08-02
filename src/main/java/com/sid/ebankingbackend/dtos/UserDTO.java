package com.sid.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private List<RoleDTO> rolesDTOS;
}
