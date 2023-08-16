package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(
			ClientRepository clientRepository,
			AccountRepository accountRepository,
			TransactionRepository transactionRepository,
			LoanRepository loanRepository,
			ClientLoanRepository clientLoanRepository,
			CardRepository cardRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Richard", "Dean Anderson", "richarddanderson@mindhub.com");

			LocalDate actualDate = LocalDate.now();
			LocalDate tomorrowDate = actualDate.plusDays(1);
			LocalDateTime actualDateTime = LocalDateTime.now();
			LocalDate thruDate = actualDate.plusYears(5);

			Account account1 = new Account("VIN001", actualDate, 5000);
			Account account2 = new Account("VIN002", tomorrowDate, 7500);
			Account account3 = new Account("VIN003", actualDate, 50000);
			Account account4 = new Account("VIN004", actualDate, 1100000);
			Account account5 = new Account("VIN005", actualDate, 250000);

			//Saves clients and generates their primary keys.
			clientRepository.save(client1);
			clientRepository.save(client2);


			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);
			client2.addAccount(account5);


			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1200000, "Transferencia Banco Nación", actualDateTime);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -150000, "Débito automático servicios", actualDateTime);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 30000, "Depósito cajero", actualDateTime);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 100000, "Transferencia Banco Macro", actualDateTime);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 920000, "Transferencia Banco Hipotecario", actualDateTime);
			Transaction transaction6 = new Transaction(TransactionType.CREDIT, 500000, "Transferencia Banco Industrial", actualDateTime);
			Transaction transaction7 = new Transaction(TransactionType.DEBIT, -20000, "Pago servicios", actualDateTime);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account2.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account3.addTransaction(transaction7);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);

			Loan loan1 = new Loan("Hipotecario", 500000, List.of((short) 12, (short) 24, (short) 36, (short) 48, (short) 60));
			Loan loan2 = new Loan("Personal", 100000, List.of((short) 6, (short) 12, (short) 24));
			Loan loan3 = new Loan("Automotriz", 300000, List.of((short) 6, (short) 12, (short) 24, (short) 36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			//double amount, short payments, Client client, Loan loan
			ClientLoan clientLoan1 = new ClientLoan(400000, (short) 60, client1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, (short) 12, client1, loan2);
			ClientLoan clientLoan3 = new ClientLoan(100000, (short) 24, client2, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000, (short) 36, client2, loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);


			//CardType type, long number, Short cvv, LocalDate fromDate, LocalDate thruDate, String cardHolder, CardColor color
			Card card1 = new Card(CardType.DEBIT, 5834128475236942l,(short) 514, actualDate, thruDate, client1.getLastName() + " " + client1.getFirstName(), CardColor.GOLD);
			Card card2 = new Card(CardType.CREDIT, 2014003526180217l,(short) 137, actualDate, thruDate, client1.getLastName() + " " + client1.getFirstName(), CardColor.TITANIUM);
			Card card3 = new Card(CardType.DEBIT, 6901205401124815l,(short) 614, actualDate, thruDate, client2.getLastName() + " " + client2.getFirstName(), CardColor.SILVER);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);


		};

	}
}
