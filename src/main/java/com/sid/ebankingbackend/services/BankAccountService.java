package com.sid.ebankingbackend.services;

import com.sid.ebankingbackend.dtos.*;
import com.sid.ebankingbackend.entites.BankAccount;
import com.sid.ebankingbackend.entites.CurrentAccount;
import com.sid.ebankingbackend.entites.Customer;
import com.sid.ebankingbackend.entites.SavingAccount;
import com.sid.ebankingbackend.exception.BankAccountNotFoundException;
import com.sid.ebankingbackend.exception.BlanceNotSufficentException;
import com.sid.ebankingbackend.exception.CustomerNotFoundException;
import com.sid.ebankingbackend.repositories.BankAccountRepository;

import java.util.List;

public interface BankAccountService {

     CustomerDTO saveCustomer (CustomerDTO customerDTO,String username);
//     CurrentBankAccountDTO saveCurrentBankAccount(Long customerId,double initialBalance, double overDraft) throws CustomerNotFoundException;
//     SavingBankAccountDTO saveSavingCurrentBankAccount(Long customerId,double initialBalance, double interestRate) throws CustomerNotFoundException;

     CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

     SavingBankAccountDTO saveSavingCurrentBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

     List<CustomerDTO> listCustomers();
     BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
     void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BlanceNotSufficentException;

     void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
     void transfer(String accountIdSource,String accountIdDestination,double amount) throws BlanceNotSufficentException, BankAccountNotFoundException;

     List<BankAccountDTO> bankAccountList();

     CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

     CustomerDTO updateCustomer(CustomerDTO customerDTO);

     void deleteCustomer(Long customerId);

     List<AccountOperationDTO> accountHistory(String accountId);

     AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

     List<CustomerDTO> searchCustomers(String keyword);
     BankAccountDTO saveAccount (BankAccountDTO bankAccountDTO);




}
