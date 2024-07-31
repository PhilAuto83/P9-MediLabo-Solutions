package com.phildev.front.mls.service;

import com.phildev.front.mls.error.DiagnosticNotFoundException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = DiabeteService.class)
class DiabeteServiceTest {

    @Autowired
    private DiabeteService diabeteService;

    @MockBean
    private MicroserviceDiabeteProxy microserviceDiabeteProxy;

    @ParameterizedTest(name = "Test récupération du niveau de risque {0}")
    @ValueSource(strings={"None", "Borderline", "Early onset", "In Danger"})
    void testCalculRisqueDiabete(String niveauRisque){
        when(microserviceDiabeteProxy.calculNiveauRisqueDiabete(5)).thenReturn(niveauRisque);
        Assertions.assertEquals(niveauRisque, diabeteService.calculDiagnosticDiabete(5));
    }

    @Test
    @DisplayName("Test gestion de l'exception si le niveau de risque ne peut être calculé")
    void testExceptionAvecImpossibiliteDeCalculerNiveauDeRisque(){
        when(microserviceDiabeteProxy.calculNiveauRisqueDiabete(10)).thenThrow(ResponseNotFoundException.class);
        Assertions.assertThrows(DiagnosticNotFoundException.class, ()-> diabeteService.calculDiagnosticDiabete(10));
    }
}
