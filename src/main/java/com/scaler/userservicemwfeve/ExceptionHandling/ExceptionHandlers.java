package com.scaler.userservicemwfeve.ExceptionHandling;

import com.scaler.userservicemwfeve.Exceptions.IncorrectPasswordException;
import com.scaler.userservicemwfeve.Exceptions.UserNotFoundException;
import com.scaler.userservicemwfeve.dtos.IncorrectPasswordExceptionHandlingDTO;
import com.scaler.userservicemwfeve.dtos.UserNotFoundExceptionHandlingDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    public ResponseEntity<UserNotFoundExceptionHandlingDTO> handleUserNotFoundException(UserNotFoundException exception) {
        UserNotFoundExceptionHandlingDTO exceptionHandlingDTO = new UserNotFoundExceptionHandlingDTO();
        exceptionHandlingDTO.setMessage("User Not Found");
        return new ResponseEntity<>(exceptionHandlingDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectPasswordExceptionHandlingDTO> handleIncorrectPasswordException(IncorrectPasswordException exception) {
        IncorrectPasswordExceptionHandlingDTO exceptionHandlingDTO = new IncorrectPasswordExceptionHandlingDTO();
        exceptionHandlingDTO.setMessage("Incorrect Password");
        return new ResponseEntity<>(exceptionHandlingDTO, HttpStatus.FORBIDDEN);
    }
}
