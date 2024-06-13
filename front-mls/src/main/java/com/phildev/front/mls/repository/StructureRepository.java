package com.phildev.front.mls.repository;

import com.phildev.front.mls.model.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StructureRepository extends JpaRepository<Structure, Integer> {
}
