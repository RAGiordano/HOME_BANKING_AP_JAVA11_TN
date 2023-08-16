package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.dtos.ClientDTO;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {
    // -------------------- Attributes --------------------
    @Autowired
    private CardRepository cardRepository;

    // -------------------- Additional methods --------------------
    @RequestMapping("/cards")
        public List<CardDTO> getCards() {
            return cardRepository.findAll().stream()
                    .map(account -> new CardDTO(account))
                    .collect(toList());
        }

        @RequestMapping("/cards/{id}")
        public CardDTO getCard(@PathVariable Long id){
            return cardRepository.findById(id)
                    .map(account ->new CardDTO(account))
                    .orElse(null);
        }
}
