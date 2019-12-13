package com.front.controller;

import com.front.Main;
import com.front.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {

        if(checkSessionTokenValidity(request)){ // Verify Connection
            model.addAttribute("header", "panel");
            return "index";
        }
        else{
            model.addAttribute("User", new User());
            return "login";
        }
    }

    @RequestMapping(value = "/application", method = RequestMethod.POST)
    public String header(@RequestParam("select") String selectHeader, Model model, HttpServletRequest request){

        if(checkSessionTokenValidity(request)){
            if(selectHeader.equals("panel")){ model.addAttribute("header", "panel");}
            if(selectHeader.equals("capteurs")){ model.addAttribute("header", "capteurs");}
            if(selectHeader.equals("utilisateurs")){ model.addAttribute("header", "utilisateurs");}
            if(selectHeader.equals("evenements")){ model.addAttribute("header", "evenements");}
            if(selectHeader.equals("alertes")){ model.addAttribute("header", "alertes");}

            return "index";
        }
        else{
            model.addAttribute("User", new User());
            return "login";
        }

    }

    public boolean checkSessionTokenValidity(HttpServletRequest request) {
        return (request.getSession().getAttribute("user") != null);
    }
}
