package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientRoleType;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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

@RestController
@RequestMapping("/api")
public class ClientController {
    // -------------------- Attributes --------------------
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --------------------- Methods ----------------------
    // Return the list of all clients data
    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.findAllClients();
    }

    // Return data of a specific client (by id)
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.findClientById(id);
    }

    @PostMapping("/clients")
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

        if (clientService.findClientByEmail(email) !=  null) {
            return new ResponseEntity<>("E-mail already in use", HttpStatus.FORBIDDEN);
        }

        try {
            // Create Client object with default role type "CLIENT"
            Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password), ClientRoleType.CLIENT);

            // Call static method generateNewAccountNumber in AccountController to generate a non-repeated account number
            String accountNumber = AccountController.generateNewAccountNumber();

            // Save client in the database and generates its primary key
            clientService.saveClient(newClient);

            LocalDate actualDate = LocalDate.now();

            // Create first account for the new client
            Account newAccount = new Account(accountNumber, actualDate, 0);

            // Add account to current client
            newClient.addAccount(newAccount);

            // Save account in the database and generates its primary key
            accountService.saveAccount(newAccount);

        // Invalid password exception
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return new ClientDTO(clientService.findClientByEmail(authentication.getName()));
    }
}
