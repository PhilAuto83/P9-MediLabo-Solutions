package com.phildev.front.mls.utils;

import com.phildev.front.mls.model.CoordonneesPatient;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;

public final class Utility {

    public static int calculAgePatient(LocalDate date){
        return (int) YEARS.between(date, LocalDate.now());
    }


}
