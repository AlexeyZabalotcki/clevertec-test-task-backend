package com.example.clevertectesttaskbackend.exception;

import java.util.NoSuchElementException;

public class NoSuchProductException extends NoSuchElementException {
    public NoSuchProductException(String message) {
        super(message);
    }

}
