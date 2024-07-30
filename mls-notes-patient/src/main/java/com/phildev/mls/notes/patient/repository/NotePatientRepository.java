package com.phildev.mls.notes.patient.repository;

import com.phildev.mls.notes.patient.model.NotePatient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotePatientRepository extends MongoRepository<NotePatient, String> {

    @Query("{'patientId': ?0}")
    List<NotePatient> findAllByPatientId(Integer patientId);

    @Query("{'patientId': ?0}")
    Page<NotePatient> findByPatientIdWithPage(Integer patientId, Pageable pageable);
}
