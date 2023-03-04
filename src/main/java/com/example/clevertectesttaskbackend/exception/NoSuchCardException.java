package com.example.clevertectesttaskbackend.exception;

import java.util.NoSuchElementException;

public class NoSuchCardException extends NoSuchElementException {
    public NoSuchCardException(String message) {
        super(message);
    }
}
