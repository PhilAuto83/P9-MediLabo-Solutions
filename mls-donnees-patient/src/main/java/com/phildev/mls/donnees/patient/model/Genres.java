package com.phildev.mls.donnees.patient.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genres {

    @Id
    private String id;

    @Field("id_genre")
    private int idGenre;

    @Field("code_genre")
    private String codeGenre;

    @Field("libelle_genre")
    private String libelleGenre;
}
