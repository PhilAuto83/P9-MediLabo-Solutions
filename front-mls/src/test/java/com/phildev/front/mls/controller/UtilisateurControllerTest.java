package com.phildev.front.mls.controller;

import com.phildev.front.mls.dto.UserDTO;
import com.phildev.front.mls.service.UtilisateurService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UtilisateurController.class)
public class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurService utilisateurService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Récupérer la liste des utilisateurs pour un ADMIN")
    public void recuperationListeUtilisateurs() throws Exception {
        when(utilisateurService.afficheUtilisateurs()).thenReturn(List.of(new UserDTO("Phil", "Dev", "USER", "Labo 1", "phil@dev.fr")));
        mockMvc.perform(get("/utilisateurs/liste"))
                .andDo(print())
                .andExpect(content().string(containsString("<title>MLS - Liste des utilisateurs</title>")))
                .andExpect(view().name("utilisateurs"))
                .andExpect(content().string(containsString("<a class=\"nav-item nav-link\" href=\"/utilisateurs/liste\">Liste des utilisateurs</a>")))
                .andExpect(content().string(containsString("<td>Phil</td>")))
                .andExpect(content().string(containsString("<td>Dev</td>")))
                .andExpect(content().string(containsString("<td>Labo 1</td>")))
                .andExpect(content().string(containsString("<td>USER</td>")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test page admin inaccessible pour un user non connu")
    public void testPageAdminInaccessiblePourUserInconnu() throws Exception {
        mockMvc.perform(get("/utilisateurs/liste"))
                .andDo(print())
                .andExpect(status().is(401));
    }
}
