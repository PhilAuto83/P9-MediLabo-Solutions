package com.phildev.front.mls.utils;

import org.springframework.ui.Model;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;

public final class Utility {

    private Utility(){}

    public static int calculAgePatient(LocalDate date){
        return (int) YEARS.between(date, LocalDate.now());
    }

    public static void ajoutAttributErreur(Model model, String nomErreur , String key){
        String message = (String)model.asMap().get(key);
        if(message != null){
            model.addAttribute(nomErreur, message);
        }
    }

}
