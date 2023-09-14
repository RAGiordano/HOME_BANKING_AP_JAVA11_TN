package com.mindhub.homebanking.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void passwordValidator() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Client.passwordValidator("test123#"), "The password must have at least one uppercase letter");
    }
}