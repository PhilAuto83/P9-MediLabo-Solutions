package com.phildev.mls.donnees.patient.repository;

import com.phildev.mls.donnees.patient.model.CoordonneesPatient;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordonneesPatientRepository extends JpaRepository<CoordonneesPatient, Long> {

    CoordonneesPatient findByNomAndPrenom(@NotNull String nom, @NotNull String prenom);


    List<CoordonneesPatient> findAllByStructureId(Integer id);

    Page<CoordonneesPatient> findAllByStructureId(Integer id, Pageable pageable);


}
