package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountDTO> findAllAccounts();
    Optional<Account> findAccountByID(Long id);
    Account findAccountByNumber(String number);
    void saveAccount (Account account);
}
