package com.front.controller;

import com.front.Main;
import com.front.entity.Area;
import com.front.entity.Coordinates;
import com.front.entity.Event;
import com.front.entity.User;
import org.json.JSONObject;
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
import static com.front.config.adresse;

@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private List<User> listUser = new ArrayList<>();
    static List<Event> listEvent = new ArrayList<>();

    @GetMapping("/")
    @Scope("session")
    public String home(Model model, HttpServletRequest request) {

        if(checkSessionTokenValidity(request)){ // Verify Connection
            mapBuilder(model);
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
            if(selectHeader.equals("panel")){ panelModel(model); mapBuilder(model); model.addAttribute("header", "panel");}
            if(selectHeader.equals("capteurs")){ showAllSensors(model); model.addAttribute("header", "capteurs");}
            if(selectHeader.equals("utilisateurs")){ userModel(model); model.addAttribute("header", "utilisateurs");}
            if(selectHeader.equals("evenements")){ eventModel(model); model.addAttribute("header", "evenements");}

            //TODO À adapter pour faire passer l'id du capteur
            if(selectHeader.equals("capteur")){ showOneSensor(model,sensorId); model.addAttribute("header", "capteur");}
            return "index";
        }
        else{
            model.addAttribute("User", new User());
            return "login";
        }
    }

    public static void mapBuilder(Model model){

        final String uri = adresse + "area/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Area[]> response = restTemplate.getForEntity(uri, Area[].class);
        Area[] areas = response.getBody();

        List<Area> listArea = Arrays.asList(areas);

        int idCount = 0;
        for (int i = 0; i < listArea.size(); i++){

            JSONObject obj = new JSONObject(listArea.get(i).getCoordinates());
            Coordinates coord = new Coordinates(obj.getDouble("x1") ,obj.getDouble("y1"),
                                             obj.getDouble("x2") ,obj.getDouble("y2"),
                                             obj.getDouble("x3") ,obj.getDouble("y3"),
                                             obj.getDouble("x4") ,obj.getDouble("y4"));

            listArea.get(i).setId(idCount);
            listArea.get(i).setCoordinatesXY(coord);
            idCount++;
        }

        model.addAttribute("listArea", listArea);
    }


    public void eventModel(Model model){

        final String uri = adresse + "event/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Event[]> response = restTemplate.getForEntity(uri, Event[].class);
        Event[] events = response.getBody();

        listEvent = Arrays.asList(events);

        int cpt = 0;
        for(Event e : listEvent){
            cpt++;
            e.setId(cpt);
        }

        model.addAttribute("listEvent", listEvent);

    }

    public static void panelModel(Model model){

        final String uri = adresse + "/user/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
        User[] users = response.getBody();

        model.addAttribute("NbUsers", String.valueOf(users.length));
    }

    public void userModel(Model model){

        final String uri = adresse + "/user/all";

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

        final String uri = adresse + "/user/update?email=" + email;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(uri, User.class);

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteUser(Model model, @RequestParam("email") String email, @RequestParam("password") String password,  HttpServletRequest request){

        String user = request.getSession().getAttribute("user").toString();
        log.info(user);

        final String uri = adresse + "/user/delete?admin=" + user + "&email=" + email;

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
