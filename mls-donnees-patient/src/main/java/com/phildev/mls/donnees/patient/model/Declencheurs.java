package com.phildev.mls.donnees.patient.model;

import java.util.Arrays;
import java.util.List;

/**
 * Cette classe est une classe utilitaire qui présente une liste statique des déclencheurs à rechercher pour générer le rapport Diabète
 */
public final class Declencheurs {

    public static final List<String> DECLENCHEURS = Arrays.asList("Hémoglobine A1C","Microalbumine","Fumeur","Fumeuse","Taille","Poids","Anormal",
            "Cholestérol","Vertiges","Rechute","Réaction","Anticorps");
}
