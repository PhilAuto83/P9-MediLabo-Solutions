package com.phildev.front.mls.controller;

import com.phildev.front.mls.dto.UserDTO;
import com.phildev.front.mls.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/utilisateurs/liste")
    public ModelAndView afficheUtilisateurs(){
        List<UserDTO> userDTOS = utilisateurService.afficheUtilisateurs();
        ModelAndView mav = new ModelAndView("utilisateurs");
        mav.addObject("utilisateurs", userDTOS);
        return mav;
    }
}
