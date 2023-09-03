package com.mindhub.homebanking;

import com.mindhub.homebanking.controllers.AccountController;
import com.mindhub.homebanking.controllers.CardController;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(
			ClientRepository clientRepository,
			AccountRepository accountRepository,
			TransactionRepository transactionRepository,
			LoanRepository loanRepository,
			ClientLoanRepository clientLoanRepository,
			CardRepository cardRepository) {
		return (args) -> {
			LocalDate actualDate = LocalDate.now();
			LocalDate tomorrowDate = actualDate.plusDays(1);
			LocalDateTime actualDateTime = LocalDateTime.now();
			LocalDate thruDate = actualDate.plusYears(5);

			// Creates Client objects
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123456"), ClientRoleType.CLIENT);
			Client client2 = new Client("Richard", "Dean Anderson", "richarddanderson@mindhub.com", passwordEncoder.encode("654321"), ClientRoleType.CLIENT);
			Client client3 = new Client("Admin", "Admin", "admin@mindhub.com", passwordEncoder.encode("admin"), ClientRoleType.ADMIN);

			// Creates Account objects
			Account account1 = new Account(AccountController.generateNewAccountNumber(), actualDate, 5000);
			Account account2 = new Account(AccountController.generateNewAccountNumber(), tomorrowDate, 7500);
			Account account3 = new Account(AccountController.generateNewAccountNumber(), actualDate, 50000);
			Account account4 = new Account(AccountController.generateNewAccountNumber(), actualDate, 1100000);
			Account account5 = new Account(AccountController.generateNewAccountNumber(), actualDate, 250000);
			Account account6 = new Account(AccountController.generateNewAccountNumber(), actualDate, 90000000);

			// Saves clients in the database and generates their primary keys.
			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			// Adds each account to its client
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);
			client2.addAccount(account5);
			client3.addAccount(account6);

			// Saves accounts in the database and generates its primary keys.
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);
			accountRepository.save(account6);

			// Creates Transaction objects
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1200000, "Galicia bank transfer", actualDateTime);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -150000, "Service debit", actualDateTime);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 30000, "ATM deposit", actualDateTime);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 100000, "Macro Bank transfer", actualDateTime);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 920000, "Galicia Bank transfer", actualDateTime);
			Transaction transaction6 = new Transaction(TransactionType.CREDIT, 500000, "Industrial Bank transfer", actualDateTime);
			Transaction transaction7 = new Transaction(TransactionType.DEBIT, -20000, "Service debit", actualDateTime);

			// Adds each transaction to its account
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account2.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account3.addTransaction(transaction7);

			// Saves transactions in the database and generates its primary keys
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);

			// Creates Loan objects
			Loan loan1 = new Loan("Mortgage Loan", 500000, List.of((short) 12, (short) 24, (short) 36, (short) 48, (short) 60));
			Loan loan2 = new Loan("Personal Loan", 100000, List.of((short) 6, (short) 12, (short) 24));
			Loan loan3 = new Loan("Car Loan", 300000, List.of((short) 6, (short) 12, (short) 24, (short) 36));

			// Saves loans in the database and generates its primary keys
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			// Creates ClientLoan objects
			ClientLoan clientLoan1 = new ClientLoan(400000, (short) 60, client1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, (short) 12, client1, loan2);
			ClientLoan clientLoan3 = new ClientLoan(100000, (short) 24, client2, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000, (short) 36, client2, loan3);

			// Saves clientLoans in the database and generates its primary keys
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			// Adds each clientLoan to its client
			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			// Creates Card objects
			Card card1 = new Card(CardType.DEBIT, CardController.generateCardNumber(),CardController.generateCVV(), actualDate, thruDate, client1.getLastName() + " " + client1.getFirstName(), CardColor.GOLD);
			Card card2 = new Card(CardType.CREDIT, CardController.generateCardNumber(),CardController.generateCVV(), actualDate, thruDate, client1.getLastName() + " " + client1.getFirstName(), CardColor.TITANIUM);
			Card card3 = new Card(CardType.DEBIT, CardController.generateCardNumber(),CardController.generateCVV(), actualDate, thruDate, client2.getLastName() + " " + client2.getFirstName(), CardColor.SILVER);

			// Adds each card to its client
			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			// Saves cards in the database and generates its primary keys
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};

	}
}
