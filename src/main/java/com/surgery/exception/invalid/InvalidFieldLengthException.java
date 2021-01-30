package com.surgery.exception.invalid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class InvalidFieldLengthException extends RuntimeException {
    public InvalidFieldLengthException(String message) {
        super(message);
    }
}
