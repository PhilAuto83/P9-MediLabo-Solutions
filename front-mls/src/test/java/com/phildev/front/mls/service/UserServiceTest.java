package com.phildev.front.mls.service;

import com.phildev.front.mls.model.User;
import com.phildev.front.mls.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserService.class)
@ActiveProfiles("Test")
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Test retour de la liste de tous les utilisateurs de l'application")
    void testRecuperationTousLesUtilisateurs(){
        when(userRepository.findAll()).thenReturn(List.of(new User(7,"Test", "Tessss",4,"USER", "test@email.fr", "dfqdfdf46dsfdfq&"),
                new User(8,"Testee", "Test",9,"USER", "testee@email.fr", "dfqdfdf46dsfdfq&")));
        List<User> users = userService.findAll();
        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(9, users.getLast().getStructureId());
        Assertions.assertEquals("Testee", users.getLast().getFirstName());
        Assertions.assertEquals("USER", users.getLast().getRole());
        Assertions.assertEquals("dfqdfdf46dsfdfq&", users.getLast().getPassword());
        Assertions.assertEquals("Testee", users.getLast().getFirstName());
        Assertions.assertEquals("test@email.fr", users.getFirst().getEmail());
        Assertions.assertEquals(4, users.getFirst().getStructureId());
    }

    @Test
    @DisplayName("Test récupération d'un utilisateur via son email")
    void testRecuperationUtilisateurParEmail(){
        when(userRepository.findByEmail("test@test.fr")).thenReturn(new User(7,"Test", "Tessss",4,"USER", "test@test.fr", "dfqdfdf46dsfdfq&"));
        User user = userService.findByEmail("test@test.fr");
        Assertions.assertEquals("test@test.fr", user.getEmail());
        Assertions.assertEquals(4, user.getStructureId());
        Assertions.assertEquals("Test", user.getFirstName());
        Assertions.assertEquals("USER", user.getRole());
        Assertions.assertEquals("dfqdfdf46dsfdfq&", user.getPassword());
        Assertions.assertEquals(7, user.getId());
    }

    @Test
    @DisplayName("Test runtime exception pour utilisateur non existant")
    void testRuntimeExceptionPourUtilisateurNonExistant(){
        when(userRepository.findByEmail("test@test.fr")).thenReturn(null);
        Assertions.assertThrows(RuntimeException.class, ()-> userService.findByEmail("test@test.fr"));
    }
}
