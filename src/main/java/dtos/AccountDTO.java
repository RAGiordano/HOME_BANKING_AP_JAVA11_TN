package dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;

public class AccountDTO {
    // -------------------- Attributes --------------------
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    //private Client owner;

    // -------------------- Constructors --------------------
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = getBalance();
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

    /*public Client getOwner() {
        return owner;
    }*/
}
