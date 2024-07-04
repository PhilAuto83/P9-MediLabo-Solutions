package com.phildev.front.mls.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Le nom ne peut pas être vide ou nul")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide ou nul")
    private String prenom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La date ne peut pas être vide et doit respecter le format yyyy-MM-dd")
    private LocalDate dateDeNaissance;

    @Pattern(regexp = "[MF]", message = "Vous devez choisir entre masculin et féminin")
    private String genre;

    private String adresse;


    private String telephone;
}
