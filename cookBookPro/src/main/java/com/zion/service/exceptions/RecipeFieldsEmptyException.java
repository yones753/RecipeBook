package com.zion.service.exceptions;

public class RecipeFieldsEmptyException extends RuntimeException {

    public RecipeFieldsEmptyException(String message) {
        super(message);
    }
}