package com.phildev.front.mls.service;


import com.phildev.front.mls.error.DiagnosticNotFoundException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DiabeteService {
    private final MicroserviceDiabeteProxy microserviceDiabeteProxy;

    public DiabeteService(MicroserviceDiabeteProxy microserviceDiabeteProxy) {
        this.microserviceDiabeteProxy = microserviceDiabeteProxy;
    }

    public String calculDiagnosticDiabete(Integer patientId){
        try{
            log.info("Appel du service back end mls-diabete via la gateway pour calcul du diagnostic du patient {}", patientId);
            return microserviceDiabeteProxy.calculNiveauRisqueDiabete(patientId);
        }catch(ResponseNotFoundException exception){
            log.error("Les informations patient sont insuffisantes pour calculer le diagnostic : {}", exception.getMessage());
            throw new DiagnosticNotFoundException("Les données pour le calcul du rapport ne sont pas présentes, veuillez ajouter des notes pour le patient "+patientId);
        }
    }
}
