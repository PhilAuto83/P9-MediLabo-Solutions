package com.phildev.front.mls.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TelephoneFormatException extends RuntimeException {
    public TelephoneFormatException(String message) {
        super(message);
    }
}
