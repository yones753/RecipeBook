package com.zion.service.exceptions;

public class IngredientPriceInUseException extends RuntimeException {

    public IngredientPriceInUseException(String message) {
        super(message);
    }
}
