package com.zion.service.exceptions;

public class RecipeUpdateException extends RuntimeException {

    public RecipeUpdateException(String message) {
        super(message);
    }

    public RecipeUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
