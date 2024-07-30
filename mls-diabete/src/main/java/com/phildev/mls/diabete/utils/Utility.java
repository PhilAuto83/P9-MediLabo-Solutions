package com.phildev.mls.diabete.utils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.YEARS;

public final class Utility {
    public static int calculAgePatient(LocalDate date){
        return (int) YEARS.between(date, LocalDate.now());
    }

    public static List<String> conversionNoteEnListeDeMots(String note){
        return Arrays.asList(note.split(" "));
    }
}

