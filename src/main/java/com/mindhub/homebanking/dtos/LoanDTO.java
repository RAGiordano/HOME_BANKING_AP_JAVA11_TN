package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class LoanDTO {
    // -------------------- Attributes --------------------
    private long id;
    private String name;
    private double maxAmount;
    private List<Short> payments = new ArrayList<>();


    // -------------------- Constructors --------------------
    public LoanDTO (Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments().stream().collect(toList());
        //this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toSet());
    }

    // -------------------- Getters --------------------
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Short> getPayments() {
        return payments;
    }
}
