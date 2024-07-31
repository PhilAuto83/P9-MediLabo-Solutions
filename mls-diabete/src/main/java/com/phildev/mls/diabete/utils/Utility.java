package com.phildev.mls.diabete.utils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.YEARS;

public final class Utility {

    /**
     * Cette méthode permet de calculer l'âge du patient en fonction de sa date de naissance
     * @param date qui est la date de naissance du patient sous forme d'objet LocalDate
     * @return un entier qui est l'âge calculé
     */
    public static int calculAgePatient(LocalDate date){
        return (int) YEARS.between(date, LocalDate.now());
    }

    /**
     * Cette méthode permet de transformer une chaîne de caractères en liste de mots. Les séparateurs de mots recherchés sont les virgules, les points et les espaces.
     * @param note qui est la note saisie et parsée
     * @return une liste de mots
     */
    public static List<String> conversionNoteEnListeDeMots(String note){
        String regex =  "[" + Pattern.quote(" ") + Pattern.quote(".") + Pattern.quote(",") + "]";
        return Arrays.asList(note.split(regex));
    }
}

