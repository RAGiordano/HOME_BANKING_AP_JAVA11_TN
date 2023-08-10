package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Richard", "Dean Anderson", "richarddanderson@mindhub.com");

			LocalDate actualDate = LocalDate.now();
			LocalDate tomorrowDate = actualDate.plusDays(1);
			LocalDateTime actualDateTime = LocalDateTime.now();

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

		};

	}
}
