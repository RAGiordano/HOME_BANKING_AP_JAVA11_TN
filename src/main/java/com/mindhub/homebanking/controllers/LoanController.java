package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    // -------------------- Attributes --------------------
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // -------------------- Additional methods --------------------
    @RequestMapping("/loans") //GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream()
                .map(loan -> new LoanDTO(loan))
                .collect(toList());
    }

    @Transactional
    @RequestMapping(value="/loans", method = RequestMethod.POST) //PostMapping(value="/loans")
    public ResponseEntity<Object> addLoan(Authentication authentication,
                                          @RequestBody LoanApplicationDTO loanApplicationDTO) {

        double amount = loanApplicationDTO.getAmount();

        // Check that the parameters are not empty.
        if (loanApplicationDTO.getLoanId() <= 0 || amount <= 0 || loanApplicationDTO.getPayments() <= 0 || loanApplicationDTO.getToAccountNumber().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        // Check that the loan exist
        if (!loanRepository.existsById(loanApplicationDTO.getLoanId())) {
            return new ResponseEntity<>("No Loan matches the requested ID", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId());

        // Check that the requested amount does not exceed the maximum loan amount
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The requested amount exceeds the maximum allowed", HttpStatus.FORBIDDEN);
        }

        Account toAccount = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());

        // Check that the target account exist
        if (toAccount == null) {
            return new ResponseEntity<>("Target account does not exist", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findById(accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber()).getClient().getId());
        Client currentClient = clientRepository.findByEmail(authentication.getName());

        // Check that the target account belongs to the authenticated client
        if (client.getId() != currentClient.getId()) {
            return new ResponseEntity<>("The target account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }


        // --- LOAN APPROVED ---

        // Creates ClientLoan objects
        ClientLoan newClientLoan = new ClientLoan(amount * 1.2,
                                                    loanApplicationDTO.getPayments(),
                                                    client,
                                                    loan);

        // Saves clientLoans in the database and generates its primary keys
        clientLoanRepository.save(newClientLoan);

        // Adds each clientLoan to its client
        client.addClientLoan(newClientLoan);

        // Create Transaction objects
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(), loan.getName(), LocalDateTime.now());

        // Add transaction to its account
        toAccount.addTransaction(creditTransaction);

        // Save transaction in the database and generate its primary key
        transactionRepository.save(creditTransaction);

        // Update account balance
        toAccount.setBalance(toAccount.getBalance() + amount);

        return new ResponseEntity<>("Loan successfully granted", HttpStatus.CREATED);
    }



}
