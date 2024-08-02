package com.sid.ebankingbackend.web;

import com.sid.ebankingbackend.dtos.*;
import com.sid.ebankingbackend.exception.BankAccountNotFoundException;
import com.sid.ebankingbackend.exception.BlanceNotSufficentException;
import com.sid.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {

    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
    return bankAccountService.getBankAccount(accountId);
}
@GetMapping("/accounts")
public List<BankAccountDTO> listAccounts(){
    return bankAccountService.bankAccountList();
}
@GetMapping("/accounts/{accountId}/operations")
public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
return bankAccountService.accountHistory(accountId);
}
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId, @RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }
    @PostMapping("/account/debit")
public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BlanceNotSufficentException, BankAccountNotFoundException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
}
    @PostMapping("/account/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BlanceNotSufficentException, BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }
    @PostMapping("/account/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BlanceNotSufficentException, BankAccountNotFoundException {
        this.bankAccountService.transfer(transferRequestDTO.getAccountSource(),transferRequestDTO.getAccountDestination(),transferRequestDTO.getAmount());

    }
//    @GetMapping("/customer/{customerId}")
//    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
//    public List<BankAccountDTO> bankAccountListCustomer(@PathVariable Long customerId) {
//        return bankAccountService.getAccountsCustomer(customerId);
//    }
//    @GetMapping("/operations")
//    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
//    public List<AccountOperationDTO> accountOperations(){
//        return bankAccountService.accountOperationsList();
//    }
}
