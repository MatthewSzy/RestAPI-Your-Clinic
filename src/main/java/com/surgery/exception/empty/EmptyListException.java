package com.surgery.exception.empty;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmptyListException extends RuntimeException {
    public EmptyListException(String message) {
        super(message);
    }
}
