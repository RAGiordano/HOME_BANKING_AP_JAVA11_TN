package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;

public class AccountDTO {
    // -------------------- Attributes --------------------
    private long id;
    private String number;
    private LocalDate date;
    private double balance;
    //private Client owner;

    // -------------------- Constructors --------------------
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.date = account.getCreationDate();
        this.balance = account.getBalance();
    }

    // -------------------- Getters --------------------

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getBalance() {
        return balance;
    }

    /*public Client getOwner() {
        return owner;
    }*/
}
