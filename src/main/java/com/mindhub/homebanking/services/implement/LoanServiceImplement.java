package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mindhub.homebanking.services.LoanService;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImplement implements LoanService {
    // -------------------- Attributes --------------------
    @Autowired
    private LoanRepository loanRepository;

    // --------------------- Methods ----------------------
    @Override
    public List<LoanDTO> findAllLoans() {
        return loanRepository.findAll().stream()
                .map(loan -> new LoanDTO(loan))
                .collect(toList());
    }

    @Override
    public Optional<Loan> findLoanById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public boolean existsLoanById(Long id) {
        return loanRepository.existsById(id);
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
