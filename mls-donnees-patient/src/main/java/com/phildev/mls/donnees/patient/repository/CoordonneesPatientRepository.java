package com.phildev.mls.donnees.patient.repository;

import com.phildev.mls.donnees.patient.model.CoordonneesPatient;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordonneesPatientRepository extends MongoRepository<CoordonneesPatient, String> {

    CoordonneesPatient findByNomAndPrenom(@NotNull String nom, @NotNull String prenom);

    List<CoordonneesPatient> findAllByStructureId(Integer id);
}
