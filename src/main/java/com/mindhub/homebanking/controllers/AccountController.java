package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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

@RestController
@RequestMapping("/api")
public class AccountController {
    // -------------------- Attributes --------------------
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    private static Set<String> existingAccountNumbers = new HashSet<>();

    // --------------------- Methods ----------------------
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.findAllAccounts();
    }

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication) {
        Account account = accountService.findAccountByID(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account != null) {
            // Check if the account belongs to the current client
            if (clientService.findClientByEmail(authentication.getName()).getAccounts().contains(account)) {
                return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> getAccounts(Authentication authentication) {
            return new ResponseEntity<>(clientService.findClientByEmail(authentication.getName()).getAccounts(), HttpStatus.OK);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccount(Authentication authentication) {
        // Check if the client has 3 accounts.
        if (clientService.findClientByEmail(authentication.getName()).getAccounts().size() >= 3) {
            return new ResponseEntity<>("The account cannot be added. The maximum allowed is three accounts per client.", HttpStatus.FORBIDDEN);
        } else {
            // Call function accountNumberGenerator to generate a non-repeated account number
            String accountNumber = generateNewAccountNumber();

            LocalDate actualDate = LocalDate.now();

            // Create account
            Account account1 = new Account(accountNumber, actualDate, 0);

            // Add account to current client
            clientService.findClientByEmail(authentication.getName()).addAccount(account1);

            // Save account
            accountService.saveAccount(account1);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    // Generate a non-repeated account number
    public static String generateNewAccountNumber() {
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



