package com.phildev.mls.notes.patient.exception;

public class NoteNonTrouveeException extends RuntimeException {
    public NoteNonTrouveeException(String message) {
        super(message);
    }
}
