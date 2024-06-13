package com.phildev.mls.donnees.patient.repository;

import com.phildev.mls.donnees.patient.model.Genres;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenresRepository extends MongoRepository<Genres, String> {
}
