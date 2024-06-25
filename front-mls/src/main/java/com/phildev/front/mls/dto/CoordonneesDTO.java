package com.phildev.front.mls.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordonneesDTO {


    private Integer structureId;

    private String nom;

    private String prenom;

    private LocalDate dateDeNaissance;

    private String genre;

    private String adresse;

    private String telephone;
}
