package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientRoleType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    // -------------------- Attributes --------------------
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    // -------------------- Additional methods --------------------
    // Return the list of all clients data
    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }

    // Return data of a specific client (by id)
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(client -> new ClientDTO(client))
                .orElse(null);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        if (firstName.isEmpty()){
            return new ResponseEntity<>("First name is required", HttpStatus.FORBIDDEN);
        }
        if (lastName.isEmpty()){
            return new ResponseEntity<>("Last name is required", HttpStatus.FORBIDDEN);
        }

        if (email.isEmpty()){
            return new ResponseEntity<>("E-mail is required", HttpStatus.FORBIDDEN);
        }

        // Regular expression to verify e-mail format
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            return new ResponseEntity<>("Invalid e-mail format", HttpStatus.FORBIDDEN);
        }

        if (password.isEmpty()) {
            return new ResponseEntity<>("Password is required", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("E-mail already in use", HttpStatus.FORBIDDEN);
        }

        // Create Client object with default role type "CLIENT"
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password), ClientRoleType.CLIENT);

        // Call static method generateNewAccountNumber in AccountController to generate a non-repeated account number
        String accountNumber = AccountController.generateNewAccountNumber();

        // Save client in the database and generates its primary key
        clientRepository.save(newClient);

        // Create first account for the new client
        Account account1 = new Account(accountNumber, LocalDate.now(), 0);

        // Add account to current client
        newClient.addAccount(account1);

        // Save account in the database and generates its primary key
        accountRepository.save(account1);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
}
