package com.front.controller;

import com.front.Main;
import com.front.entity.Capteur;
import com.front.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private List<User> listUser = new ArrayList<>();

    @GetMapping("/")
    @Scope("session")
    public String home(Model model, HttpServletRequest request) {

        if(checkSessionTokenValidity(request)){ // Verify Connection
            map(model);
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
    public String header(@RequestParam("select") String selectHeader, @RequestParam(name = "sensorId", required = false) String sensorId, Model model, HttpServletRequest request){

        if(checkSessionTokenValidity(request)){
            if(selectHeader.equals("panel")){ panelModel(model); map(model); model.addAttribute("header", "panel");}
            if(selectHeader.equals("capteurs")){showAllSensors(model); model.addAttribute("header", "capteurs");}
            if(selectHeader.equals("utilisateurs")){ AllUser(model); model.addAttribute("header", "utilisateurs");}
            if(selectHeader.equals("evenements")){ model.addAttribute("header", "evenements");}
            if(selectHeader.equals("capteur")){showOneSensor(model,sensorId); model.addAttribute("header", "capteur");}
            return "index";
        }
        else{
            model.addAttribute("User", new User());
            return "login";
        }
    }

	public static void map(Model model){

        // final String uri = "http://35.206.157.216:8080/";

        //TODO Mapping des coordonnées

        model.addAttribute("X1", 50.326770);
        model.addAttribute("Y1", 3.509654);
        model.addAttribute("X2", 50.326654);
        model.addAttribute("Y2", 3.511006);
        model.addAttribute("X3", 50.325743);
        model.addAttribute("Y3", 3.510619);
        model.addAttribute("X4", 50.325770);
        model.addAttribute("Y4", 3.509375);

    }

    public static void panelModel(Model model){

        final String uri = "http://35.206.157.216:8080/user/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
        User[] users = response.getBody();

        model.addAttribute("NbUsers", String.valueOf(users.length));
    }

    public void AllUser(Model model){

        final String uri = "http://35.206.157.216:8080/user/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
        User[] users = response.getBody();
        listUser = Arrays.asList(users);

        int cpt = 0;
        for(User u : listUser){
            cpt++;
            u.setId(cpt);
        }

        model.addAttribute("Users", listUser);

        //log.info(listUser.toString());
    }

    @RequestMapping(value = "/delete/{email}")
    public void updateUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpServletRequest request){

        final String uri = "http://35.206.157.216:8080/user/update?email=" + email;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(uri, User.class);

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteUser(Model model, @RequestParam("email") String email, @RequestParam("password") String password,  HttpServletRequest request){

        String user = request.getSession().getAttribute("user").toString();
        log.info(user);

        final String uri = "http://35.206.157.216:8080/user/delete?admin=" + user + "&email=" + email;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(uri, HttpMethod.DELETE, User.class);

    }

    public boolean checkSessionTokenValidity(HttpServletRequest request) {
        return (request.getSession().getAttribute("user") != null);
    }
    
    public void showOneSensor(Model model, String sensorId) {
		//TODO En attente de l'API, récupération des données d'un capteur, 
    	
    	
    	/*final String uri ="http://35.206.157.216:8080/capteur?id=" + sensorId;
    	RestTemplate restTemplate = new RestTemplate();
    	
        ResponseEntity<Capteur> response = restTemplate.getForEntity(uri, Capteur.class);
        Capteur capteur = response.getBody();
    	
    	model.addAttribute("capteur", capteur);*/
    	
    	
    	model.addAttribute("capteur", sensorId);	// uniquement avec les données brutes, à supprimer lorsqu'on récuperera les données
    	
	}
    
    public void showAllSensors(Model model) {
    	//TODO En attente de l'API, récupération de tous les capteurs  
    	/*
    	final String uri = "http://35.206.157.216:8080/capteurs";
    	RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Capteur[]> response = restTemplate.getForEntity(uri, Capteur[].class);
        Capteur[] capteurs = response.getBody();
        
        model.addAttribute("capteurs", capteurs);
        */
      
	}
}
