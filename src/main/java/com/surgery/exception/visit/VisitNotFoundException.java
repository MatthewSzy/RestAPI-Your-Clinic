package com.surgery.exception.visit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class VisitNotFoundException extends RuntimeException{
    public VisitNotFoundException(String message) {
        super(message);
    }
}
