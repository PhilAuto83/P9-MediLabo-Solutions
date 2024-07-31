package com.phildev.mls.diabete.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotePatient {

    private String id;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime dateCreation;

    private Integer patientId;

    private String patient;

    private String note;
}
