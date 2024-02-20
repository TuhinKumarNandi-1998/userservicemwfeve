package com.scaler.userservicemwfeve.Exceptions;

import org.springframework.data.jpa.repository.JpaRepository;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
