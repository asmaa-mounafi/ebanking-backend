package com.sid.ebankingbackend.web;

import com.sid.ebankingbackend.dtos.BankAccountDTO;
import com.sid.ebankingbackend.dtos.CustomerDTO;
import com.sid.ebankingbackend.entites.CurrentAccount;
import com.sid.ebankingbackend.entites.Customer;
import com.sid.ebankingbackend.entites.SavingAccount;
import com.sid.ebankingbackend.enums.AccountStatus;
import com.sid.ebankingbackend.exception.CustomerNotFoundException;
import com.sid.ebankingbackend.repositories.BankAccountRepository;
import com.sid.ebankingbackend.repositories.CustomerRepository;
import com.sid.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
@GetMapping("/customers")
@PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();
    }
    @GetMapping("/customers/search")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword){
        return bankAccountService.searchCustomers("%"+keyword+"%");
    }
@GetMapping("/customers/{id}")
@PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
return bankAccountService.getCustomer(customerId);
    }
@PostMapping("/customers")
@PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public CustomerDTO saveCustomer (@RequestBody CustomerDTO customerDTO, Principal principal){
    bankAccountService.saveCustomer(customerDTO,principal.getName());

    customerRepository.findAll().forEach(customer->{
        CurrentAccount currentAccount =new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(Math.random()*90000);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(90000);
        bankAccountRepository.save(currentAccount);

        SavingAccount savingAccount =new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(Math.random()*90000);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(5.5);
        bankAccountRepository.save(savingAccount);
    });
    return customerDTO;

    }
    @PutMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public CustomerDTO updateCustomer(@PathVariable Long customerId ,@RequestBody CustomerDTO customerDTO){
customerDTO.setId(customerId);
return bankAccountService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public void deleteCustomer(@PathVariable Long id){

        bankAccountService.deleteCustomer(id);

    }

}
