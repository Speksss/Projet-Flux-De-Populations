package com.front.controller;

import com.front.Main;
import com.front.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @GetMapping("/connexion")
    public String login(Model model) {

        model.addAttribute("User", new User());
        return "login";
    }

    @RequestMapping(value = "/connexion", method = RequestMethod.POST)
    @Scope("session")
    public String connexion(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpServletRequest request){


        final String uri = "http://localhost:8080/login?email="+ email + "&password=" + password;

        RestTemplate restTemplate = new RestTemplate();
        User result = restTemplate.getForObject(uri, User.class);

        if(result != null){

            // TODO Session User
            request.getSession().setAttribute("user", email);
            Object connected = request.getSession().getAttribute("user");
            return "index";
        }
        else{
            model.addAttribute("error", true);
            return "login";
        }
    }
}
