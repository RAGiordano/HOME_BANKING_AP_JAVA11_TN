package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;

import java.util.List;
import java.util.Set;

public interface CardService {
    List<CardDTO> findAllCards();

    CardDTO findCardById(Long id);

    void saveCard(Card card);

    Set<String> getExistingCardNumbers();
}
