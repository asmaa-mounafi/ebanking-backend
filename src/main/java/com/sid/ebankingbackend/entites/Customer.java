package com.sid.ebankingbackend.entites;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sid.ebankingbackend.securite.entitie.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "customer")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<BankAccount> bankAccounts;
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
}
