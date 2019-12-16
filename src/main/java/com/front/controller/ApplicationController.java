package com.front.controller;

import com.front.Main;
import com.front.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @GetMapping("/")
    @Scope("session")
    public String home(Model model, HttpServletRequest request) {

        if(checkSessionTokenValidity(request)){ // Verify Connection
            panelModel(model);
            model.addAttribute("header", "panel");
            return "index";
        }
        else{
            model.addAttribute("User", new User());
            return "login";
        }
    }

    @RequestMapping(value = "/application", method = RequestMethod.POST)
    @Scope("session")
    public String header(@RequestParam("select") String selectHeader, Model model, HttpServletRequest request){

        if(checkSessionTokenValidity(request)){
            if(selectHeader.equals("panel")){ panelModel(model); model.addAttribute("header", "panel");}
            if(selectHeader.equals("capteurs")){ model.addAttribute("header", "capteurs");}
            if(selectHeader.equals("utilisateurs")){ model.addAttribute("header", "utilisateurs");}
            if(selectHeader.equals("evenements")){ model.addAttribute("header", "evenements");}

            return "index";
        }
        else{
            model.addAttribute("User", new User());
            return "login";
        }
    }

    public void panelModel(Model model){

        final String uri = "http://35.206.157.216:8080/user/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
        User[] users = response.getBody();

        model.addAttribute("NbUsers", String.valueOf(users.length));
    }

    public boolean checkSessionTokenValidity(HttpServletRequest request) {
        return (request.getSession().getAttribute("user") != null);
    }
}
