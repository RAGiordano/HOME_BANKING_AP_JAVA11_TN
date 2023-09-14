package com.mindhub.homebanking.utils;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class CardUtils {



    private static Set<String> existingCardNumbers = new HashSet<>();

    private CardUtils() {
    }

    public static void fillExistingCardNumbers(Set<String> numbers) {
        // Fills existingCardNumbers set
        existingCardNumbers.addAll(numbers);
    }

    public static short generateCVV() {
        Random random = new Random();
        return (short) (random.nextInt(900) + 100);
    }

    public static String generateCardNumber() {
        Random random = new Random();
        String newCardNumber;

        do {
            newCardNumber = String.format("%04d", random.nextInt(10000)) + "-" +
                    String.format("%04d", random.nextInt(10000)) + "-" +
                    String.format("%04d", random.nextInt(10000)) + "-" +
                    String.format("%04d", random.nextInt(10000));
        } while (existingCardNumbers.contains(newCardNumber));

        return newCardNumber;
    }

    public static void addExistingCardNumbers(String number) {
        existingCardNumbers.add(number);
    }
}
