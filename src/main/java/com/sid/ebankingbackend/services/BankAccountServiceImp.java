package com.sid.ebankingbackend.services;

import com.sid.ebankingbackend.dtos.*;
import com.sid.ebankingbackend.entites.*;
import com.sid.ebankingbackend.enums.OperationType;
import com.sid.ebankingbackend.exception.BankAccountNotFoundException;
import com.sid.ebankingbackend.exception.BlanceNotSufficentException;
import com.sid.ebankingbackend.exception.CustomerNotFoundException;
import com.sid.ebankingbackend.mappers.BankAccountMapperImpl;
import com.sid.ebankingbackend.repositories.AccountOperationRepository;
import com.sid.ebankingbackend.repositories.BankAccountRepository;
import com.sid.ebankingbackend.repositories.CustomerRepository;
import com.sid.ebankingbackend.securite.entitie.User;
import com.sid.ebankingbackend.securite.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImp implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
private BankAccountMapperImpl dtoMapper;
private AppUserRepository appUserRepository;
private BankAccountMapperImpl mapper;



    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO,String username) {
       log.info("Saving new Customer");
        User user = appUserRepository.findByUsername(username);
        Customer customer = mapper.fromCustomersDTO(customerDTO);
        customer.setUser(user);
        Customer saveCustomer = customerRepository.save(customer);
        CustomerDTO customerDTO1 = mapper.fromCustomer(saveCustomer);

        return customerDTO1;

//       Customer customer=dtoMapper.fromCustomersDTO(customerDTO);
//      Customer savedCustomer =  customerRepository.save(customer);
//        return dtoMapper.fromCustomer(savedCustomer);
    }



    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount=new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
       currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
CurrentAccount savedBankAccount= bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingCurrentBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount=new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount= bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savingAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers= customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());

//        List<CustomerDTO> customerDTOS = new ArrayList<>();
//        for (Customer customer:customers){
//           CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
        //CustomerDTOS.add(customerDTO).
//        }
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }

    }

    @Override
    public void debit(String accountId, double amount,String description) throws BankAccountNotFoundException, BlanceNotSufficentException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));if (bankAccount.getBalance()<amount)
    throw new BlanceNotSufficentException("Balance not sufficient");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
    accountOperation.setOperationDate(new Date());
    accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
bankAccount.setBalance(bankAccount.getBalance()-amount);
bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount,String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BlanceNotSufficentException, BankAccountNotFoundException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);

    }
    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts= bankAccountRepository.findAll();
       List<BankAccountDTO> bankAccountDTOS= bankAccounts.stream().map(account ->{
           if(account instanceof SavingAccount){
              SavingAccount savingAccount=(SavingAccount) account;
               return dtoMapper.fromSavingBankAccount(savingAccount);
           }else{
               CurrentAccount currentAccount=(CurrentAccount) account;
               return dtoMapper.fromCurrentBankAccount(currentAccount);
           }
        }).collect(Collectors.toList());
       return bankAccountDTOS;
    }
@Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer=dtoMapper.fromCustomersDTO(customerDTO);
        Customer savedCustomer =  customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
@Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }
@Override
public List<AccountOperationDTO> accountHistory(String accountId){
    List<AccountOperation> accountOperations=    accountOperationRepository.findByBankAccountId(accountId);
   return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
}

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
  BankAccount bankAccount =bankAccountRepository.findById(accountId).orElse(null);
  if(bankAccount==null) throw new BankAccountNotFoundException("Account not found");
   Page<AccountOperation> accountOperations= accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page,size));
AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
List<AccountOperationDTO> accountOperationDTOS= accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
accountHistoryDTO.setAccountId(bankAccount.getId());
accountHistoryDTO.setBalance(bankAccount.getBalance());
accountHistoryDTO.setCurrentPage(page);
accountHistoryDTO.setPageSize(size);
accountHistoryDTO.setTotalPage(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers=customerRepository.searchCustomer(keyword);
       List<CustomerDTO> customerDTOS = customers.stream().map(cust->dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
       return customerDTOS;
    }

    @Override
    public BankAccountDTO saveAccount(BankAccountDTO bankAccountDTO) {
        log.info("Saving new Account");
        BankAccount bankAccount=dtoMapper.fromCurrentBankAccountDTO((CurrentBankAccountDTO) bankAccountDTO);
        BankAccount savedAccount =  bankAccountRepository.save(bankAccount);
        return dtoMapper.fromSavingBankAccount((SavingAccount) savedAccount);
    }

}
