package com.credit.dossier.exception;

public class EmailProcessingException extends RuntimeException{
    public EmailProcessingException(String message) {
        super(message);
    }
}
