package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    // -------------------- Attributes --------------------
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    private static Set<String> existingAccountNumbers = new HashSet<>();

    // -------------------- Additional methods --------------------
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream()
                .map(account -> new AccountDTO(account))
                .collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        //Checks if the account belongs to the current client
        if (clientRepository.findByEmail(authentication.getName()).getAccounts().contains(account)) {
            return new AccountDTO(account);
        } else {
            throw new RuntimeException("Unauthorized access to account");
        }
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccount(Authentication authentication) {
        //Checks if the client has 3 accounts.
        if (clientRepository.findByEmail(authentication.getName()).getAccounts().size() >= 3) {
            return new ResponseEntity<>("The account cannot be added. The maximum allowed is three accounts per client.", HttpStatus.FORBIDDEN);
        } else {
            //Calls function accountNumberGenerator to generate a non-repeated account number
            String accountNumber = generateNewAccountNumber();

            //Creates account
            Account account1 = new Account(accountNumber, LocalDate.now(), 0);

            //Adds account to current client
            clientRepository.findByEmail(authentication.getName()).addAccount(account1);

            //Saves account
            accountRepository.save(account1);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    //Generates a non-repeated account number
    public static String generateNewAccountNumber() { // List<Client> clients) {
        //Generates a collection of existing account numbers
        /*Set<String> existingAccountNumbers = clients.stream()
                .flatMap(client -> client.getAccounts().stream())
                .map(account -> account.getNumber())
                .collect(Collectors.toSet());*/

        Random random = new Random();
        String newAccountNumber;
        do {
            int randomNumber = random.nextInt(90000000) + 10000000; // 8-digit number
            newAccountNumber = "VIN-" + randomNumber;
        } while (existingAccountNumbers.contains(newAccountNumber));

        existingAccountNumbers.add(newAccountNumber);

        return newAccountNumber;
    }

}



