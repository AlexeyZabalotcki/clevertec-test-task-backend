package com.example.clevertectesttaskbackend.exception;

import java.util.NoSuchElementException;

public class NoSuchReceiptException extends NoSuchElementException {
    public NoSuchReceiptException(String message) {
        super(message);
    }
}
