package com.sid.ebankingbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sid.ebankingbackend.entites.BankAccount;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;

}
