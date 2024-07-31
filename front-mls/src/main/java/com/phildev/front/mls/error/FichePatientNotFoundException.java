package com.phildev.front.mls.error;


public class FichePatientNotFoundException extends RuntimeException {
    public FichePatientNotFoundException(String message) {
        super(message);
    }
}
