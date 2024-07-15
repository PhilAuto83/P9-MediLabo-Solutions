package com.phildev.mls.notes.patient.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document(collection = "notes-patient")
@Data
public class NotePatient {

    @Id
    private String id;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime creation_date;

    private Integer patient_id;

    private String patient;

    private String note;

}
