package com.phildev.front.mls.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordonneesPatient {

    private Long id;

    private Integer structureId;

    private String nom;


    private String prenom;


    private LocalDate dateDeNaissance;

    private String genre;


    private String adresse;

    private String telephone;
}
