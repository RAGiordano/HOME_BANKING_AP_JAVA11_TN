package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Richard", "Dean Anderson", "richarddanderson@mindhub.com");

			LocalDate fechaActual = LocalDate.now();
			LocalDate fechaManana = fechaActual.plusDays(1);

			Account account1 = new Account("VIN001", fechaActual, 5000);
			Account account2 = new Account("VIN002", fechaManana, 7500);
			Account account3 = new Account("VIN003", fechaActual, 50000);
			Account account4 = new Account("VIN004", fechaActual, 1100000);
			Account account5 = new Account("VIN005", fechaActual, 250000);

			//Guardo mis clientes y se les generan las claves primarias.
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
		};

	}
}
