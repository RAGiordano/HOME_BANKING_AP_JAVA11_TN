package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {

    // --------------- generateCardNumber Tests ---------------
    @Test
    public void cardNumberIsCreatedTest(){
        String cardNumber = CardUtils.generateCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void generateCardNumberLengthTest() {
        String cardNumber = CardUtils.generateCardNumber();
        assertThat(cardNumber.length(), is(equalTo(19)));
    }

    // ------------------ generateCVV Tests -------------------
    @Test
    public void generateCVVRangeTest() {
        short cvv = CardUtils.generateCVV();
        assertThat(cvv, allOf(greaterThanOrEqualTo((short) 100), lessThanOrEqualTo((short) 999)));
    }

    @Test
    public void generateCVVNumberOfDigitsTest() {
        short cvv = CardUtils.generateCVV();
        assertThat(String.valueOf(cvv).length(), is(equalTo(3)));
    }
}
