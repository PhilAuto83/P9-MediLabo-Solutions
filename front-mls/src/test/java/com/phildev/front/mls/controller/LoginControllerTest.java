package com.phildev.front.mls.controller;

import com.phildev.front.mls.model.User;
import com.phildev.front.mls.service.StructureService;
import com.phildev.front.mls.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private StructureService structureService;

    @Test
    @WithMockUser(roles ="ADMIN", username = "admin@mls.fr")
    @DisplayName("Test de l'affichage de la page de l admin")
    void testAffichageAdminPage() throws Exception {
        when(structureService.getStructureNameById(any())).thenReturn("MLS");
        when(userService.findByEmail("admin@mls.fr")).thenReturn(new User(20, "Phil", "Dev", 2, "ADMIN", "admin@mls.fr", "pwd"));
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(view().name("user/admin"))
                .andExpect(content().string(containsString("<title>MLS - Admin</title>")))
                .andExpect(content().string(containsString("<div>Bienvenue sur la page administrateur MLS : Phil Dev</div>")))
                .andExpect(content().string(containsString("<div>Vous faites partie de la structure : MLS</div>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser( username = "user@mls.fr")
    @DisplayName("Test de l'affichage de la page d un utilisateur classique")
    void testAffichageHomePageUser() throws Exception {
        when(structureService.getStructureNameById(any())).thenReturn("Cabinet Dr Sam");
        when(userService.findByEmail("user@mls.fr")).thenReturn(new User(20, "Phil", "Dev", 3, "USER", "user@mls.fr", "pwd"));
        mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(view().name("user/home"))
                .andExpect(content().string(containsString("<a class=\"nav-item nav-link active\" href=\"/home\">Home</a>")))
                .andExpect(content().string(containsString("<a class=\"nav-item nav-link\" href=\"/patient/ajout\">Ajouter un patient</a>")))
                .andExpect(content().string(containsString("<a class=\"nav-item nav-link\" href=\"/patients/liste\">Liste des patients</a>")))
                .andExpect(content().string(containsString("<div>Bienvenue sur la home page MLS : Phil Dev</div>")))
                .andExpect(content().string(containsString("<title>MLS - Home</title>")))
                .andExpect(content().string(containsString("<div>Bienvenue sur la home page MLS : Phil Dev</div>")))
                .andExpect(content().string(containsString("<div>Vous faites partie de la structure : Cabinet Dr Sam</div>")))
                .andExpect(status().isOk());
    }

}
