package com.surgery.exception.wrong;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class WrongPatientException extends RuntimeException {
    public WrongPatientException(String message) {
        super(message);
    }
}
