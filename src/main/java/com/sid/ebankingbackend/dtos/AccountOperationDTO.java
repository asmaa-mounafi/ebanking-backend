package com.sid.ebankingbackend.dtos;

import com.sid.ebankingbackend.entites.BankAccount;
import com.sid.ebankingbackend.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private String description;
    private  double amount;
    private OperationType type;


}
