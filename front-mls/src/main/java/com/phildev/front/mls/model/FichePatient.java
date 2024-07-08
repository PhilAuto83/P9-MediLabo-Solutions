package com.phildev.front.mls.model;

import lombok.Data;

@Data
public class FichePatient {

    private Long id;
    private Integer structureId;
    private String nom;
    private String prenom;
    private int age;
    private String adresse;
    private String telephone;
}
