package com.learning.app.ws.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class GenerateId {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateId(int length) {
        StringBuilder generateString = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            generateString.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return new String(generateString);
    }
}
