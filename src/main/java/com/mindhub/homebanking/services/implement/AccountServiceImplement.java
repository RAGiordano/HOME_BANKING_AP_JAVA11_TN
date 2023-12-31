package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {
    // -------------------- Attributes --------------------
    @Autowired
    private AccountRepository accountRepository;

    // --------------------- Methods ----------------------
    @Override
    public void saveAccount (Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> findAllAccounts() {
        return accountRepository.findAll().stream()
                .map(account -> new AccountDTO(account))
                .collect(toList());
    }

    @Override
    public Optional<Account> findAccountByID(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account findAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }
}
