package com.phildev.mls.donnees.patient.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Document(collection = "coordonnees-patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordonneesPatient {

    @Id
    private String id;

    @NotNull(message = "ne doit pas être nul")
    @Field("structure_id")
    private Integer structureId;

    @NotBlank(message = "ne doit pas être nul ou vide")
    private String nom;

    @NotBlank(message = "ne doit pas être nul ou vide")
    private String prenom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "ne doit pas être vide ou nulle et doit respecter le format yyyy-MM-dd")
    private LocalDate dateDeNaissance;


    private String adresse;

    private String telephone;


}
