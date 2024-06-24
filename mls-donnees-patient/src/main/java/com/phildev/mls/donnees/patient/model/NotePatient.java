package com.phildev.mls.donnees.patient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "notes-patient")
public class NotePatient {

    @Id
    private String id;
    @NotNull
    @Field("patient_id")
    private String patientId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Field("date_creation")
    private LocalDateTime dateCreation;
    @NotNull
    private String contenu;
}
