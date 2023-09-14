package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class RepositoriesTest {

    // --------------- Loan Repository Test ---------------
    @Autowired
    LoanRepository loanRepository;

    // Verify that there are loans
    @Test
    public void existLoansTest() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }

    // Verify that there is a loan with the name "Personal Loan"
    @Test
    public void existPersonalLoanTest() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal Loan"))));
    }



    // ------------- Account Repository Test --------------
    @Autowired
    AccountRepository accountRepository;

    // Save an account in the database and retrieve its information
    @Test
    @Rollback
    public void saveAccountAndRetrieveItsInformationTest() {
        // Create Account object
        Account account = new Account("TEST-0001", LocalDate.of(2023, 9, 14), 1000000);

        // Save account in the database and generate its primary key
        accountRepository.save(account);

        // Get the account by its number
        Account obtainedAccount = accountRepository.findByNumber("TEST-0001");

        // Check that the object obtainedAccount is not null
        assertNotNull(obtainedAccount);

        // Check that the recovered account information is correct
        assertEquals("TEST-0001", obtainedAccount.getNumber());
        assertEquals(LocalDate.of(2023, 9, 14), obtainedAccount.getCreationDate());
        assertEquals(1000000.0, obtainedAccount.getBalance(), 0.001);

    }

    // Verify that there are accounts
    @Test
    public void existAccountsTest() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }



    // ---------------- Card Repository Test ------------------
    @Autowired
    private CardRepository cardRepository;

    @Test
    @Rollback
    public void updateCardTest() {
        // Generate a new card
        Card card = new Card(CardType.CREDIT, "1111-2222-3333-4444", (short) 123, LocalDate.of(2023, 9, 14), LocalDate.of(2028, 9, 14), "Test John", CardColor.GOLD);

        // Save card in the database
        cardRepository.save(card);

        // Change some properties of the card
        card.setCardHolder("Test Jane");
        card.setCvv((short) 987);

        // Update card in the database
        cardRepository.save(card);

        // Get the card with updated data from the database
        Card updatedCard = cardRepository.findById(card.getId()).get(0);

        // Check that the card information was correctly updated
        assertNotNull(updatedCard);
        assertEquals("Test Jane", updatedCard.getCardHolder());
        assertEquals((short) 987, updatedCard.getCvv());
    }


    // --------------- Client Repository Test -----------------
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Rollback
    public void saveClientAndRetrieveItsInformationTest() {
        // Generate a new client
        Client client = new Client("John", "Test", "johntest@example.com", "pwTest1#", ClientRoleType.CLIENT);

        // Save client in the database
        clientRepository.save(client);

        // Get client by email
        Client obtainedClient = clientRepository.findById(client.getId());

        // Check that the object obtainedClient is not null
        assertNotNull(obtainedClient);

        // Check that the recovered client information is correct
        assertThat(obtainedClient.getFirstName(), is("John"));
        assertThat(obtainedClient.getLastName(), is("Test"));
        assertThat(obtainedClient.getEmail(), is("johntest@example.com"));
        assertThat(obtainedClient.getPassword(), is("pwTest1#"));
        assertThat(obtainedClient.getRole(), is(ClientRoleType.CLIENT));
    }

    // Verify that there are clients
    @Test
    public void existClientsTest() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }


    // ------------- Transaction Repository Test --------------

    @Autowired
    private TransactionRepository transactionRepository;
    // Verify that there are transactions
    @Test
    public void existTransactionsTest() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }
}
