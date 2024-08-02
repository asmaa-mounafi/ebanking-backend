package com.sid.ebankingbackend.services;

import com.sid.ebankingbackend.entites.BankAccount;
import com.sid.ebankingbackend.entites.CurrentAccount;
import com.sid.ebankingbackend.entites.SavingAccount;
import com.sid.ebankingbackend.repositories.BankAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount1 = bankAccountRepository.findById("a34e0fc8-1b77-4c4e-b877-9303df1bcb5b").orElse(null);
        if(bankAccount1!=null) {
            System.out.println("*******************");
            System.out.println(bankAccount1.getId());
            System.out.println(bankAccount1.getBalance());
            System.out.println(bankAccount1.getStatus());
            System.out.println(bankAccount1.getCreatedAt());
            System.out.println(bankAccount1.getCustomer().getName());
            System.out.println(bankAccount1.getClass().getSimpleName());

            if (bankAccount1 instanceof CurrentAccount) {
                System.out.println("Over Draft =>" + ((CurrentAccount) bankAccount1).getOverDraft());
            } else if (bankAccount1 instanceof SavingAccount) {
                System.out.println("Rate =>" + ((SavingAccount) bankAccount1).getInterestRate());
            }
            bankAccount1.getAccountOperations().forEach(accountOperation -> {
                System.out.println(accountOperation.getType() + "\t" + accountOperation.getOperationDate() + "\t" + accountOperation.getAmount());
            });
        }
    }
}
