package com.phildev.front.mls.controller;

import com.phildev.front.mls.dto.UserDTO;
import com.phildev.front.mls.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.List;

@RestController
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/utilisateurs/liste")
    public ModelAndView afficheUtilisateurs(){
        List<UserDTO> userDTOS = utilisateurService.afficheUtilisateurs();
        ModelAndView mav = new ModelAndView("user/utilisateurs");
        mav.addObject("utilisateurs", userDTOS);
        return mav;
    }
}
