package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class AccountDTO {
    // -------------------- Attributes --------------------
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private Set<TransactionDTO> transactions = new HashSet<>();

    //private Client client;

    // -------------------- Constructors --------------------
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toSet());
    }

    // -------------------- Getters --------------------

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions(){
        return transactions;
    }
    /*public Client getClient() {
        return client;
    }*/
}
