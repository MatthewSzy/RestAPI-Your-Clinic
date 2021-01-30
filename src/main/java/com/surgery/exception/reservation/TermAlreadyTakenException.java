package com.surgery.exception.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class TermAlreadyTakenException extends RuntimeException {
    public TermAlreadyTakenException(String message) {
        super(message);
    }
}
