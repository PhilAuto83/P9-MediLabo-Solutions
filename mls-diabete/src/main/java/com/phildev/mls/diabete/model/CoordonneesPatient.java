package com.phildev.mls.diabete.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordonneesPatient {
    private Long id;
    private Integer structureId;
    private String nom;
    private String prenom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDeNaissance;
    private String genre;
    private String adresse;
    private String telephone;
}

