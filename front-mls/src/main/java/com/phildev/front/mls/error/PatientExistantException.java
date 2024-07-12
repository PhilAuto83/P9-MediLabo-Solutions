package com.phildev.front.mls.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PatientExistantException extends RuntimeException {
    public PatientExistantException(String message) {
        super(message);
    }
}
