package com.phildev.mls.donnees.patient.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "coordonnees-patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordonneesPatient {

    @Id
    private String id;

    private String nom;

    @Field("prénom")
    private String prenom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateDeNaissance;

    private String adresse;

    @Field("téléphone")
    private String telephone;


}
