package com.phildev.mls.notes.patient.repository;

import com.phildev.mls.notes.patient.model.NotePatient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotePatientRepository extends MongoRepository<NotePatient, String> {
}
