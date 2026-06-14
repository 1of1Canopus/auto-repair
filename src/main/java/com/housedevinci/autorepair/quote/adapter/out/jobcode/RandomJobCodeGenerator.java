package com.housedevinci.autorepair.quote.adapter.out.jobcode;

import com.housedevinci.autorepair.quote.application.port.out.JobCodeGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomJobCodeGenerator implements JobCodeGenerator {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 10;

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        StringBuilder code = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            code.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }
}
