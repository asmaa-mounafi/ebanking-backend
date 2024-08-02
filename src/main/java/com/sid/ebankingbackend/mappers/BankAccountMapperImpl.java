package com.sid.ebankingbackend.mappers;

import com.sid.ebankingbackend.dtos.*;
import com.sid.ebankingbackend.entites.AccountOperation;
import com.sid.ebankingbackend.entites.CurrentAccount;
import com.sid.ebankingbackend.entites.Customer;
import com.sid.ebankingbackend.entites.SavingAccount;
import com.sid.ebankingbackend.securite.Response.UserResponse;
import com.sid.ebankingbackend.securite.entitie.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer){
       CustomerDTO customerDTO=new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
//       customerDTO.setId(customer.getId());
//       customerDTO.setName(customer.getName());
//       customerDTO.setEmail(customerDTO.getEmail());
        return  customerDTO;

    }
    public Customer fromCustomersDTO(CustomerDTO customerDTO){
      Customer customer = new Customer();
      BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount){
SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
return savingBankAccountDTO;
    }
    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO){
SavingAccount savingAccount=new SavingAccount();
BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
savingAccount.setCustomer(fromCustomersDTO(savingBankAccountDTO.getCustomerDTO()));
return savingAccount;
    }
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount){
CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());

return currentBankAccountDTO;
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){
CurrentAccount currentAccount=new CurrentAccount();
BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
currentAccount.setCustomer(fromCustomersDTO(currentBankAccountDTO.getCustomerDTO()));
return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(
                accountOperation,accountOperationDTO
        );
        return accountOperationDTO;
    }
    public User fromUserDTO(UserDTO UserDTO){
        User user = new User();
        BeanUtils.copyProperties(UserDTO,user);
        return user;
    }
    public UserDTO fromAppUser(User appUser){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(appUser,userDTO);
        return userDTO;
    }
    public UserResponse fromAppUserDTOResponse(UserDTO UserDTO){
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(UserDTO.getName());
        userResponse.setEmail(UserDTO.getEmail());
        return userResponse;
    }
}
