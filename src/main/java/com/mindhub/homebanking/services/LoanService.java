package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<LoanDTO> findAllLoans();
    Optional<Loan> findLoanById(Long id);
    boolean existsLoanById(Long id);
    void saveLoan(Loan loan);
}
