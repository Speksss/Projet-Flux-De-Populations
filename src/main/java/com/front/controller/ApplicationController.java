package com.front.controller;

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

    @GetMapping("/application")
    public String home(Model model, HttpServletRequest request) {

        checkSessionTokenValidity(request); // Verify Connection
        return "index";
    }

    @RequestMapping(value = "/application", method = RequestMethod.POST)
    public String header(@RequestParam("select") String selectHeader, Model model, HttpServletRequest request){

        checkSessionTokenValidity(request); // Verify Connection

        if(selectHeader.equals("panel")){ model.addAttribute("header", "panel");}
        if(selectHeader.equals("manage")){ model.addAttribute("header", "manage");}
        if(selectHeader.equals("gestion-utilisateurs")){ model.addAttribute("header", "gestion-utilisateurs");}
        if(selectHeader.equals("evenements")){ model.addAttribute("header", "evenements");}
        if(selectHeader.equals("alertes")){ model.addAttribute("header", "alertes");}

        return "index";
    }

    public boolean checkSessionTokenValidity(HttpServletRequest request) {
        return (request.getSession().getAttribute("user") != null && (new Date().getTime() < (Long)request.getSession().getAttribute("user")));
    }
}
