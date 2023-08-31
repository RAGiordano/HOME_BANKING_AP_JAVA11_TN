package com.mindhub.homebanking.repositories;

import java.util.List;
import java.util.Optional;
import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findById(Long id);
    Account findByNumber(String number);
}




