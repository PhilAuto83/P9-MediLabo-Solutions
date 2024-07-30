package com.phildev.mls.diabete.model;

public enum NiveauDeRisque {
    AUCUN("None"),
    LIMITE("Borderline"),
    DANGER("In Danger"),
    PRECOCE("Early onset");

    private final String diagnostic;

    @Override
    public String toString() {
        return diagnostic;
    }
    NiveauDeRisque(String diagnostic){
        this.diagnostic = diagnostic;
    }
}
