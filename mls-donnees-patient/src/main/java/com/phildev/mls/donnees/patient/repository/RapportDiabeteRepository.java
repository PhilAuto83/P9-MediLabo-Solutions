package com.phildev.mls.donnees.patient.repository;

import com.phildev.mls.donnees.patient.model.RapportDiabete;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RapportDiabeteRepository extends MongoRepository<RapportDiabete, String> {
}
