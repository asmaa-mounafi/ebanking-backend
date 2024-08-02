package com.sid.ebankingbackend.entites;

import com.sid.ebankingbackend.enums.OperationType;
import com.sid.ebankingbackend.securite.entitie.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AccountOperation {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private Date operationDate;
    private String description;
    private  double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
@ManyToOne
    private BankAccount bankAccount;
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;


}
