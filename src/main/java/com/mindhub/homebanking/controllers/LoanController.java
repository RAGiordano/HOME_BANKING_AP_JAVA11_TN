package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api")
public class LoanController {
    // -------------------- Attributes --------------------
    @Autowired
    private ClientService clientService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    // --------------------- Methods ----------------------
    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.findAllLoans();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> addLoan(Authentication authentication,
                                          @RequestBody LoanApplicationDTO loanApplicationDTO) {

        double amount = loanApplicationDTO.getAmount();

        // Check that the parameters are not empty.
        if (loanApplicationDTO.getLoanId() == 0 || amount == 0 || loanApplicationDTO.getPayments() == 0 || isNull(loanApplicationDTO.getPayments()) || isNull(loanApplicationDTO.getToAccountNumber()) || loanApplicationDTO.getToAccountNumber().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        // Check that the parameters are negative.
        if (loanApplicationDTO.getLoanId() < 0 || amount < 0 || loanApplicationDTO.getPayments() < 0) {
            return new ResponseEntity<>("Negative values are not allowed", HttpStatus.FORBIDDEN);
        }

        // Check that the loan exist
        if (!loanService.existsLoanById(loanApplicationDTO.getLoanId())) {
            return new ResponseEntity<>("No loan matches the requested ID", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.findLoanById(loanApplicationDTO.getLoanId()).orElse(null);

        // Check that the requested amount does not exceed the maximum loan amount
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The requested amount exceeds the maximum allowed", HttpStatus.FORBIDDEN);
        }

        Account toAccount = accountService.findAccountByNumber(loanApplicationDTO.getToAccountNumber());

        // Check that the target account exist
        if (toAccount == null) {
            return new ResponseEntity<>("Target account does not exist", HttpStatus.FORBIDDEN);
        }

        ClientDTO clientDTO = clientService.findClientById(accountService.findAccountByNumber(loanApplicationDTO.getToAccountNumber()).getClient().getId());
        Client currentClient = clientService.findClientByEmail(authentication.getName());

        // Check that the target account belongs to the authenticated client
        if (clientDTO.getId() != currentClient.getId()) {
            return new ResponseEntity<>("The target account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }


        // --- LOAN APPROVED ---

        // Creates ClientLoan objects
        ClientLoan newClientLoan = new ClientLoan(amount * 1.2,
                                                    loanApplicationDTO.getPayments(),
                                                    currentClient,
                                                    loan);

        // Saves clientLoans in the database and generates its primary keys
        clientLoanService.saveClientLoan(newClientLoan);

        // Adds each clientLoan to its client
        currentClient.addClientLoan(newClientLoan);

        // Create Transaction objects
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(), loan.getName(), LocalDateTime.now());

        // Add transaction to its account
        toAccount.addTransaction(creditTransaction);

        // Save transaction in the database and generate its primary key
        transactionService.saveTransaction(creditTransaction);

        // Update account balance
        toAccount.setBalance(toAccount.getBalance() + amount);

        return new ResponseEntity<>("Loan successfully granted", HttpStatus.CREATED);
    }



}
