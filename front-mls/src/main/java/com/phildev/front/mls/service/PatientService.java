package com.phildev.front.mls.service;


import com.phildev.front.mls.dto.CoordonneesDTO;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.User;
import com.phildev.front.mls.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;

    /**
     * Cette méthode permet de récupérer tous les coordonnées des patients associés à la structure de l'utilisateur connecté
     * @param principal qui est l'objet permettant de récupérer le user connecté et son structure id
     * @return une liste de {@link CoordonneesDTO} qui représente les coordonnées d'un patient (téléphone, adresse, nom, prénom)
     */
    public List<CoordonneesDTO> recupereToutesLesCoordonneesPatient(Principal principal) {
        List<CoordonneesDTO> coordonneesDTOList = new ArrayList<>();
        User user = userRepository.findByEmail(principal.getName());
        List<CoordonneesPatient> coordonneesPatients = microserviceCoordonneesPatientProxy.recupereCoordonneesParStructure(user.getStructureId());
        coordonneesPatients.forEach(coordonneesPatient -> {
            CoordonneesDTO coordonneesDTO = new CoordonneesDTO();
            coordonneesDTO.setNom(coordonneesPatient.getNom());
            coordonneesDTO.setPrenom(coordonneesPatient.getPrenom());
            coordonneesDTO.setDateDeNaissance(coordonneesPatient.getDateDeNaissance());
            coordonneesDTO.setAdresse(coordonneesPatient.getAdresse());
            coordonneesDTO.setGenre(coordonneesPatient.getGenre());
            coordonneesDTO.setTelephone(coordonneesPatient.getTelephone());
            coordonneesDTOList.add(coordonneesDTO);
        });
        return coordonneesDTOList;
    }
}
