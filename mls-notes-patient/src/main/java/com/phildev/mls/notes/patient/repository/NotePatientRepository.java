package com.phildev.mls.notes.patient.repository;

import com.phildev.mls.notes.patient.model.NotePatient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotePatientRepository extends MongoRepository<NotePatient, String> {

    @Query("{'patient_id': ?0}")
    List<NotePatient> findByPatientId(Integer patientId);
}
