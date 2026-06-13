package com.housedevinci.autorepair.quote.application;

import java.util.UUID;

public class QuoteNotFoundException extends RuntimeException {

    public QuoteNotFoundException(UUID id) {
        super("Quote " + id + " not found");
    }
}
