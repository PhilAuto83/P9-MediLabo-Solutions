package com.phildev.mls.donnees.patient.model;

import lombok.Getter;

public enum NiveauRisque {

    NONE("aucun risque"),
    BORDERLINE("risque limité"),
    IN_DANGER("danger"),
    EARLY_On_SET("apparition précoce");

    @Getter
    private String risque;

    NiveauRisque(String risque){
        this.risque = risque;
    }

}
