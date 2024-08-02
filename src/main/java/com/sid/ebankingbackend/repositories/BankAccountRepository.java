package com.sid.ebankingbackend.repositories;

import com.sid.ebankingbackend.entites.BankAccount;
import com.sid.ebankingbackend.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
