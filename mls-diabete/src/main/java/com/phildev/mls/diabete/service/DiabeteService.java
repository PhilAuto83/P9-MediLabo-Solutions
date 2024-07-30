package com.phildev.mls.diabete.service;

import com.phildev.mls.diabete.exception.DonneesPatientNonTrouvees;
import com.phildev.mls.diabete.exception.NoteNonTrouveeException;
import com.phildev.mls.diabete.model.CoordonneesPatient;
import com.phildev.mls.diabete.model.NiveauDeRisque;
import com.phildev.mls.diabete.model.NotePatient;
import com.phildev.mls.diabete.utils.Declencheurs;
import com.phildev.mls.diabete.utils.Utility;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DiabeteService {

    private final MicroserviceNotePatientProxy microserviceNotePatientProxy;

    private final MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;


    public DiabeteService(MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy, MicroserviceNotePatientProxy microserviceNotePatientProxy){
        this.microserviceCoordonneesPatientProxy = microserviceCoordonneesPatientProxy;
        this.microserviceNotePatientProxy = microserviceNotePatientProxy;
    }

    public String calculNiveauRisque(Integer patientId){

        if(aNiveauRisqueLimite(patientId)){
            return NiveauDeRisque.LIMITE.toString();
        }else if( aNiveauRisqueDanger(patientId)){
            return NiveauDeRisque.DANGER.toString();
        }else if(aNiveauRisquePrecoce(patientId)){
            return NiveauDeRisque.PRECOCE.toString();
        }else{
            return NiveauDeRisque.AUCUN.toString();
        }
    }

    private boolean aNiveauRisqueLimite(Integer patientId){
        Map<String, Object> infosAgeSexe = getAgeSexePatient(Long.valueOf(patientId));
        int nbDeclencheurs = getNombreDeclencheurs(patientId);
        return nbDeclencheurs <= 5 && ((int) infosAgeSexe.get("age")) > 30;
    }

    private boolean aNiveauRisqueDanger(Integer patientId){
        Map<String, Object> infosAgeSexe = getAgeSexePatient(Long.valueOf(patientId));
        int nbDeclencheurs = getNombreDeclencheurs(patientId);
        if(((int) infosAgeSexe.get("age")) > 30 && nbDeclencheurs >= 6){
            return true;
        }else if (((int) infosAgeSexe.get("age")) < 30 && infosAgeSexe.get("sexe").equals("M") && nbDeclencheurs >=3){
            return true;
        }else return ((int) infosAgeSexe.get("age")) < 30 && infosAgeSexe.get("sexe").equals("F") && nbDeclencheurs >= 4;
    }

    private boolean aNiveauRisquePrecoce(Integer patientId){
        Map<String, Object> infosAgeSexe = getAgeSexePatient(Long.valueOf(patientId));
        int nbDeclencheurs = getNombreDeclencheurs(patientId);
        if(((int) infosAgeSexe.get("age")) > 30 && nbDeclencheurs >= 8){
            return true;
        }else if (((int) infosAgeSexe.get("age")) < 30 && infosAgeSexe.get("sexe").equals("M") && nbDeclencheurs >=5){
            return true;
        }else return ((int) infosAgeSexe.get("age")) < 30 && infosAgeSexe.get("sexe").equals("F") && nbDeclencheurs >= 7;
    }

    private int getNombreDeclencheurs(Integer patientId){
        int nbDeclencheurs = 0;
        try{
            List<NotePatient> notes = microserviceNotePatientProxy.recupererLesNotesParPatient(patientId);
            List<String> notesEnListe = new ArrayList<>();
            notes.stream()
                    .map(note-> Utility.conversionNoteEnListeDeMots(note.getNote()))
                    .forEach(notesEnListe::addAll);
            for(String mot : notesEnListe){
                for(String declencheur : Declencheurs.listeDeclencheurs){
                    if(mot.equalsIgnoreCase(declencheur)){
                        nbDeclencheurs++;
                    }
                }
            }
            return nbDeclencheurs;
        }catch(FeignException exception){
            log.error("Aucune note trouvée pour le patient {}", patientId);
            throw new NoteNonTrouveeException("Aucune note trouvée pour le patient "+patientId);
        }
    }

    private Map<String, Object> getAgeSexePatient(Long patientId){
        Map<String, Object> infosAgeSexe = new HashMap<>();
        try{
            CoordonneesPatient patient = microserviceCoordonneesPatientProxy.recuperePatient(patientId);
            infosAgeSexe.put("age", Utility.calculAgePatient(patient.getDateDeNaissance()));
            infosAgeSexe.put("sexe", patient.getGenre());
            return  infosAgeSexe;
        }catch(FeignException exception){
            log.error("Les données du patient {} n'ont pas été trouvées", patientId);
            throw new DonneesPatientNonTrouvees("Les données du patient n'ont pas été trouvées");
        }
    }
}
