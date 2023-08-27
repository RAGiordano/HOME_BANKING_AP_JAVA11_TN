package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;


import java.time.LocalDate;

public class CardDTO {
    // -------------------- Attributes --------------------
    private long id;
    private CardType type;
    private String number;
    private Short cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private String cardHolder;
    private CardColor color;
    private Client client;

    // -------------------- Constructors --------------------

    public CardDTO(Card card) {
        this.id = card.getId();
        this.type = card.getType();
        this.number = new String(card.getNumber());
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.cardHolder = card.getCardHolder();
        this.color = card.getColor();
        this.client = card.getClient();
    }


    // -------------------- Getters --------------------

    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public Short getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardColor getColor() {
        return color;
    }

    /*public Client getClient() {
        return client;
    }*/


    // -------------------- Additional methods --------------------

    // -------------------- toString method --------------------

}
