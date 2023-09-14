package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class CardServiceImplement implements CardService {
    // -------------------- Attributes --------------------
    @Autowired
    private CardRepository cardRepository;


    // --------------------- Methods ----------------------
    @Override
    public List<CardDTO> findAllCards() {
        return cardRepository.findAll().stream()
                .map(card -> new CardDTO(card))
                .collect(toList());
    }

    @Override
    public CardDTO findCardById(Long id) {
        return cardRepository.findById(id)
                .map(card ->new CardDTO(card))
                .orElse(null);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Set<String> getExistingCardNumbers() {
        return cardRepository.findAll().stream()
                .map(card -> card.getNumber())
                .collect(toSet());
    }


}
