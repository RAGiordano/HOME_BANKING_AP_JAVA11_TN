package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.security.core.Authentication;
import com.mindhub.homebanking.repositories.ClientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {
    // -------------------- Attributes --------------------
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    private static Set<String> existingCardNumbers = new HashSet<>();

    // -------------------- Additional methods --------------------
    // Returns a list of all cards data
    @RequestMapping("/cards")
    public List<CardDTO> getCards() {
        return cardRepository.findAll().stream()
                .map(account -> new CardDTO(account))
                .collect(toList());
    }

    // Returns data from an specific card
    @RequestMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return cardRepository.findById(id)
                .map(account ->new CardDTO(account))
                .orElse(null);
    }

    // Generates a new card
    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardColor cardColor, CardType cardType) {

        // Gets current client
        Client currentClient = clientRepository.findByEmail(authentication.getName());

        // Checks if the client has three cards of the requested card type
        if (!currentClient.getCards().stream()
                .filter(card -> card.getType() == cardType)
                .filter(card -> card.getColor() == cardColor)
                .collect(Collectors.toSet()).isEmpty()) {
            return new ResponseEntity<>("The card cannot be added. The client already has a " + cardColor.toString() + " " + cardType.toString() + " card.", HttpStatus.FORBIDDEN);
        } else {
            LocalDate actualDate = LocalDate.now();
            LocalDate thruDate = actualDate.plusYears(5);

            // Generates new card number
            String newCardNumber = generateCardNumber();

            // Adds new cardNumber to Set existingCardNumbers
            existingCardNumbers.add(newCardNumber);

            // Generates new CVV number
            short cvv = generateCVV();

            // Creates card object
            Card newCard = new Card(cardType, newCardNumber, cvv, actualDate, thruDate, currentClient.getFirstName() + " " + currentClient.getLastName(), cardColor);

            // Adds card to current client
            currentClient.addCard(newCard);

            // Saves card
            cardRepository.save(newCard);

            // NO
            // Saves client
            //clientRepository.save(currentClient);

            // Returns responseEntity with status 201
            return new ResponseEntity<>(HttpStatus.CREATED);
        }



    }

    public static short generateCVV() {
        Random random = new Random();
        return (short) (random.nextInt(900) + 100);
    }

    public static String generateCardNumber() {
        Random random = new Random();
        String newCardNumber;

        do {
            newCardNumber = String.format("%04d", random.nextInt(10000)) + "-" +
                    String.format("%04d", random.nextInt(10000)) + "-" +
                    String.format("%04d", random.nextInt(10000)) + "-" +
                    String.format("%04d", random.nextInt(10000));
        } while (existingCardNumbers.contains(newCardNumber));

        return newCardNumber;
    }
}
