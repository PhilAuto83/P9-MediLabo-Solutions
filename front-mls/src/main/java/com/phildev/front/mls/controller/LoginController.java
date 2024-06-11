package com.phildev.front.mls.controller;

import com.phildev.front.mls.model.User;
import com.phildev.front.mls.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * This method is the login endpoint which displays the login view
     * User can enter credentials to be authenticated in the app
     * @return the login view
     */
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/login");
        return mav;
    }

    /**
     * This method is accessible to admin user and displays user list with their roles
     * @return a view user/list and send an object to the model which is the list of users
     */
    @GetMapping("/admin")
    public ModelAndView getAdminHomePage(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        ModelAndView mav = new ModelAndView();
        mav.addObject("username", user.getFirstName()+" "+user.getLastName());
        mav.setViewName("user/admin");
        return mav;
    }

    @GetMapping("/home")
    public ModelAndView getUserHomePage(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        ModelAndView mav = new ModelAndView();
        mav.addObject("username", user.getFirstName()+" "+user.getLastName());
        mav.setViewName("user/home");
        return mav;
    }


}