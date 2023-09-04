package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    // -------------------- Attributes --------------------
    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;


    // --------------------- Methods ----------------------
    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            Authentication authentication,
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber) {

        Account fromAccount = accountService.findAccountByNumber(fromAccountNumber);
        Account toAccount = accountService.findAccountByNumber(toAccountNumber);
        Client currentClient = clientService.findClientByEmail(authentication.getName());


        // Check that the parameters are not empty.
        if (amount == null || description == null || fromAccountNumber == null || toAccountNumber == null) {
            return new ResponseEntity<>("All parameters are required", HttpStatus.FORBIDDEN);
        }

        // Check that the account numbers are not the same
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("The source and destination accounts must be different", HttpStatus.FORBIDDEN);
        }

        // Check that the source account exists
        if (fromAccount == null) {
            return new ResponseEntity<>("The source account does not exist", HttpStatus.FORBIDDEN);
        }

        // Check that the source account belongs to the authenticated client
        if (currentClient.getAccounts().stream().filter(account -> account.getNumber()
                .equals(fromAccountNumber)).count() == 0) {

            return new ResponseEntity<>("The source account does not belong to current client", HttpStatus.FORBIDDEN);
        }

        // Check that the destination account exists
        if (toAccount == null) {
            return new ResponseEntity<>("The destination account does not exist", HttpStatus.FORBIDDEN);
        }

        // Check that the source account has the amount available.
        if (fromAccount.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds in the account to complete the transfer", HttpStatus.FORBIDDEN);
        }


        // --- TRANSACTION APPROVED ---

        // Create Transaction objects
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, (amount * (-1)), description + " (" + toAccountNumber +")", LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description + " (" + fromAccountNumber + ")", LocalDateTime.now());

        // Add each transaction to its account
        fromAccount.addTransaction(debitTransaction);
        toAccount.addTransaction(creditTransaction);

        // Save transactions in the database and generate its primary keys
        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        // Update accounts balance
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
    }
}
