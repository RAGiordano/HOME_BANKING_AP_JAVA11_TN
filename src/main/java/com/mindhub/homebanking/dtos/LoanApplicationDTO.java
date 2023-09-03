package com.mindhub.homebanking.dtos;



public class LoanApplicationDTO {
    // -------------------- Attributes --------------------
    private long loanId;
    private double amount;
    private Short payments;
    private String toAccountNumber;


    // -------------------- Getters --------------------


    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public Short getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
