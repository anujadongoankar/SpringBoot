package com.inrhythm.supermarket.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidItemFoundException extends RuntimeException {
    public InvalidItemFoundException(String s) {
        super(s);
    }
}
