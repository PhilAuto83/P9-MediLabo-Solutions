package com.phildev.mls.donnees.patient.repository;

import com.phildev.mls.donnees.patient.model.CoordonneesPatient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordonneesPatientRepository extends MongoRepository<CoordonneesPatient, String> {

}
