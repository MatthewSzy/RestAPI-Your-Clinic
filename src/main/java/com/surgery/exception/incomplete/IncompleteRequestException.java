package com.surgery.exception.incomplete;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class IncompleteRequestException extends RuntimeException {
    public IncompleteRequestException(String message) {
        super(message);
    }
}
