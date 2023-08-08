package dtos;

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
    Set<AccountDTO> accounts = new HashSet<>();



    // -------------------- Constructors --------------------
    public ClientDTO(Client client){
        //AccountRepository accountRepository = new AccountRepository;

        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts.addAll(client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toSet()));
    }
//Set<Account> accounts = new HashSet<>();
//return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
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

}
