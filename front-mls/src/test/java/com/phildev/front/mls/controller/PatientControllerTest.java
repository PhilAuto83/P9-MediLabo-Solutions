package com.phildev.front.mls.controller;

import com.phildev.front.mls.error.BadRequestException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.User;
import com.phildev.front.mls.service.PatientService;
import com.phildev.front.mls.service.UserService;
import com.phildev.front.mls.utils.PrincipalTest;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private UserService userService;

    private static final Principal PRINCIPAL = new PrincipalTest();



    @Test
    @WithMockUser
    @DisplayName("Récupérer la liste avec 1 patient associé à la structure")
    public void recuperationListePatients() throws Exception {
        when(patientService.recupereToutesLesCoordonneesPatient(any())).thenReturn(List.of(new CoordonneesPatient(1L, 2,"DEV", "PHIL", LocalDate.of(1988,5,21), "M","12 rue du Test", "000-555-7777")));
        mockMvc.perform(get("/patients/liste"))
                .andDo(print())
                .andExpect(content().string(containsString("<title>MLS - Liste des patients</title>")))
                .andExpect(view().name("patients"))
                .andExpect(content().string(containsString("<td>PHIL</td>")))
                .andExpect(content().string(containsString(" <td>DEV</td>")))
                .andExpect(content().string(containsString("<td>1988-05-21</td>")))
                .andExpect(content().string(containsString(" <td>M</td>")))
                .andExpect(content().string(containsString("<td>12 rue du Test</td>")))
                .andExpect(content().string(containsString("<td>000-555-7777</td>")))
                .andExpect(content().string(containsString(" <a href=\"/patient/1\">Voir fiche</a>")))
                .andExpect(content().string(containsString("<a href=\"/patient/update/1\">Mettre à jour</a>")))
                .andExpect(content().string(containsString("<a href=\"/patient/suppression/1\">Supprimer</a>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test message d'erreur avec liste vide")
    public void recuperationListePatientsVide() throws Exception {
        when(patientService.recupereToutesLesCoordonneesPatient(any())).thenThrow(ResponseNotFoundException.class);
        mockMvc.perform(get("/patients/liste"))
                .andDo(print())
                .andExpect(content().string(containsString("<title>MLS - Liste des patients</title>")))
                .andExpect(view().name("patients"))
                .andExpect(content().string(containsString("<span class=\"fs-4 text-danger\">La structure n&#39;a aucun patient pour le moment</span>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Supprimer un patient avec redirection vers la liste des patients")
    public void supprimerUnPatient() throws Exception {
        doNothing().when(patientService).supprimerPatient(2L);
        mockMvc.perform(get("/patient/suppression/2"))
                .andDo(print())
                .andExpect(view().name("redirect:/patients/liste"))
                .andExpect(status().is(302));
    }

    @Test
    @WithMockUser
    @DisplayName("Gérer l'exception lors de la suppression d'un patient")
    public void gestionExceptionSuppressionUnPatient() throws Exception {
        doThrow((RuntimeException.class)).when(patientService).supprimerPatient(2L);
        mockMvc.perform(get("/patient/suppression/2"))
                .andDo(print())
                .andExpect(content().string(containsString("<title>MLS - Liste des patients</title>")))
                .andExpect(view().name("patients"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tester@mls.fr")
    @DisplayName("Affichage du formulaire de création patient")
    public void afficherLeFormulairePatientAvecSucces() throws Exception {
        when(userService.findByEmail("tester@mls.fr")).thenReturn(new User(4, "Joe","Louis",3,  "USER","tester@mls.fr","123456"));
        mockMvc.perform(get("/patient/ajout"))
                .andDo(print())
                .andExpect(model().attributeExists("patient"))
                .andExpect(content().string(containsString("<title>MLS - Ajout patient</title>")))
                .andExpect(content().string(containsString("<div class=\"fs-4 text-center mt-2\">Ajouter le patient</div>")))
                .andExpect(content().string(containsString("<input type=\"text\" class=\"form-control\" id=\"structureId\" name=\"structureId\" value=\"3\">")))
                .andExpect(content().string(containsString("<button type=\"submit\" class=\"btn btn-primary mt-2\">Ajouter le patient</button>")))
                .andExpect(content().string(containsString("<input type=\"text\" class=\"form-control\" id=\"adresse\" placeholder=\"Entrez l'adresse du patient\" name=\"adresse\" value=\"\">")))
                .andExpect(view().name("ajout_patient"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tester@mls.fr")
    @DisplayName("Création patient valide")
    public void creerPatientValide() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(null,2, "Developer","Phil", LocalDate.of(1983, 7,  18), "M","","000-111-2222");
        when(patientService.sauvegarderUnPatient(patient)).thenReturn(patient);
        mockMvc.perform(post("/patient")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("structureId", "2")
                        .param("prenom", "Phil")
                        .param("nom", "Developer")
                        .param("genre", "M")
                        .param("dateDeNaissance", "1983-07-18")
                        .param("adresse", "")
                        .param("telephone", "000-111-2222"))
                .andDo(print())
                .andExpect(view().name("redirect:/patients/liste"))
                .andExpect(status().is(302));
    }

    @Test
    @WithMockUser(username = "tester@mls.fr")
    @DisplayName("Création patient invalide avec des champs non conformes")
    public void redirigerVersPagePatientAvecErreurApresSaisieInvalide() throws Exception {
        mockMvc.perform(post("/patient")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("structureId", "2")
                        .param("prenom", "")
                        .param("nom", "")
                        .param("genre", "")
                        .param("dateDeNaissance", "1983-17-18")
                        .param("adresse", "")
                        .param("telephone", "000-111-8888"))
                .andDo(print())
                .andExpect(view().name("ajout_patient"))
                .andExpect(content().string(containsString("<p class=\"text-danger\">Le prénom ne peut pas être vide ou nul</p>")))
                .andExpect(content().string(containsString(" <p class=\"text-danger\">Le nom ne peut pas être vide ou nul</p>")))
                .andExpect(content().string(containsString("<p class=\"text-danger\">Vous devez choisir entre masculin et féminin</p>")))
                .andExpect(content().string(containsString("<p class=\"text-danger\">Le format de la date n&#39;est pas valide, il doit respecter le format yyyy-MM-dd</p>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tester@mls.fr")
    @DisplayName("Création patient invalide avec champs téléphone invalide")
    public void redirigerVersPagePatientAvecChampsTelephoneInvalide() throws Exception {
        mockMvc.perform(post("/patient")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("structureId", "2")
                        .param("prenom", "Sofia")
                        .param("nom", "Test")
                        .param("genre", "F")
                        .param("dateDeNaissance", "1983-05-18")
                        .param("adresse", "12 rue des tests")
                        .param("telephone", "000-111"))
                .andDo(print())
                .andExpect(view().name("ajout_patient"))
                .andExpect(content().string(containsString("<p class=\"text-danger\">Le numéro de téléphone doit respecter le format suivant 111-444-7777</p>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tester@mls.fr")
    @DisplayName("Création patient invalide lors d'une exception BadRequestException levée côté backend")
    public void redirigerVersPagePatientAvecBadRequestException() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(null,2, "Test","Sofia", LocalDate.of(1983, 5,  18), "F","12 rue des tests","000-111-8888");

        when(patientService.sauvegarderUnPatient(patient)).thenThrow(new BadRequestException("Erreur du backend"));
        mockMvc.perform(post("/patient")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("structureId", "2")
                        .param("prenom", "Sofia")
                        .param("nom", "Test")
                        .param("genre", "F")
                        .param("dateDeNaissance", "1983-05-18")
                        .param("adresse", "12 rue des tests")
                        .param("telephone", "000-111-8888"))
                .andDo(print())
                .andExpect(view().name("ajout_patient"))
                .andExpect(model().attributeExists("backendError"))
                .andExpect(content().string(containsString(" <div>Erreur du backend</div>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tester@mls.fr")
    @DisplayName("Création patient invalide lors d'une exception ResponseNotFoundException levée côté backend")
    public void redirigerVersPagePatientAvecResponseNotFoundException() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(null,2, "Test","Sofia", LocalDate.of(1983, 5,  18), "F","12 rue des tests","000-111-8888");

        when(patientService.sauvegarderUnPatient(patient)).thenThrow(new ResponseNotFoundException("Pas de patient créé"));
        mockMvc.perform(post("/patient")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("structureId", "2")
                        .param("prenom", "Sofia")
                        .param("nom", "Test")
                        .param("genre", "F")
                        .param("dateDeNaissance", "1983-05-18")
                        .param("adresse", "12 rue des tests")
                        .param("telephone", "000-111-8888"))
                .andDo(print())
                .andExpect(view().name("ajout_patient"))
                .andExpect(model().attributeExists("backendError"))
                .andExpect(content().string(containsString(" <div>Pas de patient créé</div>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tester@mls.fr")
    @DisplayName("Test affichage du formulaire pour mettre à jour le patient 6")
    public void testMiseAJourPatientValide() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(6L,2, "Test","Sofia", LocalDate.of(1983, 5,  18), "F","12 rue des tests","000-111-8888");
               when(patientService.recuperePatient(6L)).thenReturn(patient);
               mockMvc.perform(get("/patient/update/6"))
                .andDo(print())
                .andExpect(view().name("update_patient"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(content().string(containsString(" <title>MLS - Mise à jour du patient</title>")))
                .andExpect(content().string(containsString("<input type=\"text\" class=\"form-control\" id=\"structureId\" name=\"structureId\" value=\"2\">")))
                .andExpect(content().string(containsString("<input type=\"text\" class=\"form-control\" id=\"prenom\" name=\"prenom\" value=\"Sofia\">")))
                .andExpect(content().string(containsString("<input type=\"text\" class=\"form-control\" id=\"nom\" aria-describedby=\"nom\" name=\"nom\" value=\"Test\">")))
                .andExpect(content().string(containsString("<option value=\"F\" selected=\"selected\">Féminin</option>")))
                .andExpect(content().string(containsString("<input type=\"text\" class=\"form-control\" id=\"dateDeNaissance\" name=\"dateDeNaissance\" value=\"1983-05-18\">")))
                .andExpect(content().string(containsString("<input type=\"text\" class=\"form-control\" id=\"adresse\" name=\"adresse\" value=\"12 rue des tests\">")))
                .andExpect(content().string(containsString("<input type=\"text\" class=\"form-control\" id=\"telephone\" name=\"telephone\" value=\"000-111-8888\">")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tester@mls.fr")
    @DisplayName("Mise à jour patient valide")
    public void updatePatientValide() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(8L,2, "Developer","Phil", LocalDate.of(1983, 7,  18), "M","","000-111-2222");
        when(patientService.sauvegarderUnPatient(patient)).thenReturn(patient);
        mockMvc.perform(post("/patient/update/8")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("structureId", "2")
                        .param("prenom", "Phil")
                        .param("nom", "Developer")
                        .param("genre", "M")
                        .param("dateDeNaissance", "1983-07-18")
                        .param("adresse", "")
                        .param("telephone", "000-111-2222"))
                .andDo(print())
                .andExpect(view().name("redirect:/patients/liste"))
                .andExpect(status().is(302));
    }


}
