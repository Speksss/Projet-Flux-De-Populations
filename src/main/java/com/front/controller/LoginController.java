package com.front.controller;

import com.front.Main;
import com.front.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import static com.front.controller.ApplicationController.*;
import static com.front.config.adresse;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @GetMapping("/login")
    public String login(Model model) {

        model.addAttribute("User", new User());
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @Scope("session")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpServletRequest request){

        if(!email.equals("") && !password.equals("")){

            final String uri = adresse + "login?email="+ email + "&password=" + password;

            RestTemplate restTemplate = new RestTemplate();
            User response = restTemplate.getForObject(uri, User.class);

            if(response != null){
                request.getSession().setAttribute("user", email);

                mapBuilder(model);
                panelModel(model);
                model.addAttribute("header", "panel");
                return "index";
            }
        }
        else{
            model.addAttribute("User", new User());
            model.addAttribute("error", true);
        }

        return "login";

        /* TEST */
//        mapBuilder(model);
//        panelModel(model);
//        model.addAttribute("header", "panel");
//        return "index";
    }

    @GetMapping("/logout")
    @Scope("session")
    public String logout(Model model, HttpServletRequest request){

        model.addAttribute("User", new User());
        model.addAttribute("logout", true);
        request.getSession().removeAttribute("user");
        return "login";
    }
}
