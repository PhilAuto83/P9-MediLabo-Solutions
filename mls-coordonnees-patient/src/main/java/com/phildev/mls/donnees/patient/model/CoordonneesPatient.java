package com.phildev.mls.donnees.patient.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "coordonnees_patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordonneesPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "ne doit pas être nul")
    @Column(name = "structure_id")
    private Integer structureId;

    @NotBlank(message = "ne doit pas être nul ou vide")
    private String nom;

    @NotBlank(message = "ne doit pas être nul ou vide")
    private String prenom;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "ne doit pas être vide ou nulle et doit respecter le format yyyy-MM-dd")
    @Column(name = "date_de_naissance")
    private LocalDate dateDeNaissance;

    @Pattern(regexp = "[FM]", message = "Le genre doit être M ou F pour masculin ou féminin")
    private String genre;


    private String adresse;

    private String telephone;


}
