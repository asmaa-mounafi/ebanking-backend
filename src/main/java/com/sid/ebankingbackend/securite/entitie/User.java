package com.sid.ebankingbackend.securite.entitie;

import com.sid.ebankingbackend.entites.AccountOperation;
import com.sid.ebankingbackend.entites.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String userId;
    @Column(unique = true)
    private String username;
    private  String password;
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    @OneToMany(mappedBy = "user")
    private List<Customer> customers;
    @OneToMany(mappedBy = "user")
    private List<AccountOperation> accountOperations;

}
