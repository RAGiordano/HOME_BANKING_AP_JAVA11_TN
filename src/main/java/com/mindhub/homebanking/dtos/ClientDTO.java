package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

public class ClientDTO{
    // -------------------- Attributes --------------------
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO> loans = new HashSet<>();



    // -------------------- Constructors --------------------
    public ClientDTO(Client client){

        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toSet());
        //this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(toSet());
        this.loans = client.getClientLoans().stream()
                .map(clientLoan -> new ClientLoanDTO(clientLoan))
                .collect(toSet());
    }

    // -------------------- Getters --------------------

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts(){
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }
}
