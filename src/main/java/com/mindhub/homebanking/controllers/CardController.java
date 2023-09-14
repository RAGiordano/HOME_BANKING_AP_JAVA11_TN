package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CardController {
    // -------------------- Attributes --------------------
    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;




    // --------------------- Methods ----------------------
    // Return a list of all cards data
    @GetMapping("/cards")
    public List<CardDTO> getCards() {
        return cardService.findAllCards();
    }

    // Return data from an specific card
    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return cardService.findCardById(id);
    }

    // Generate a new card
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardColor cardColor, CardType cardType) {

        // Get current client
        Client currentClient = clientService.findClientByEmail(authentication.getName());

        // Check if the client has three cards of the requested card type
        if (!currentClient.getCards().stream()
                .filter(card -> card.getType() == cardType)
                .filter(card -> card.getColor() == cardColor)
                .collect(Collectors.toSet()).isEmpty()) {
            return new ResponseEntity<>("The card cannot be added. The client already has a " + cardColor.toString() + " " + cardType.toString() + " card.", HttpStatus.FORBIDDEN);
        } else {
            LocalDate actualDate = LocalDate.now();
            LocalDate thruDate = actualDate.plusYears(5);

            // Generate new card number
            String newCardNumber = CardUtils.generateCardNumber();

            // Add new cardNumber to Set existingCardNumbers
            CardUtils.addExistingCardNumbers(newCardNumber);

            // Generate new CVV number
            short cvv = CardUtils.generateCVV();

            // Create card object
            Card newCard = new Card(cardType, newCardNumber, cvv, actualDate, thruDate, currentClient.getFirstName() + " " + currentClient.getLastName(), cardColor);

            // Add card to current client
            currentClient.addCard(newCard);

            // Save card
            cardService.saveCard(newCard);

            // Return responseEntity with status 201
            return new ResponseEntity<>(HttpStatus.CREATED);
        }



    }


}
