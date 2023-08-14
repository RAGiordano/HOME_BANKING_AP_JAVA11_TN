package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    // -------------------- Attributes --------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private double maxAmount;
    @ElementCollection
    private List<Short> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    // -------------------- Constructors --------------------
    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Short> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    // -------------------- Getters & Setters --------------------


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Short> getPayments() {
        return payments;
    }

    public void setPayments(List<Short> payments) {
        this.payments = payments;
    }

    @JsonIgnore
    public List<Client> getClients() {
        return clientLoans.stream()
                .map(clientLoan -> clientLoan.getClient())
                // .map(ClientLoan::getClient)
                .collect(toList());
    }
    // -------------------- Additional methods --------------------

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }
}
