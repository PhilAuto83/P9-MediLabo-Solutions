package com.phildev.front.mls.error;

public class DiagnosticNotFoundException extends RuntimeException {
    public DiagnosticNotFoundException(String message) {
        super(message);
    }
}
