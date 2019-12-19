package com.front.controller;

import com.front.Main;
import com.front.entity.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.front.config.adresse;

@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private List<User> listUser = new ArrayList<>();
    private List<Message> listMessage = new ArrayList<>();
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
            if(selectHeader.equals("messages")){ AllMesssage(model, request); model.addAttribute("header", "messages"); }

            //TODO À adapter pour faire passer l'id du capteur
            if(selectHeader.equals("capteur")){ showOneSensor(model,sensorId); model.addAttribute("header", "capteur");}
            return "index";
        }
        else{
            model.addAttribute("User", new User());
            return "login";
        }
    }

    public static void panelModel(Model model){

        final String uri = adresse + "/user/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
        User[] users = response.getBody();

        model.addAttribute("NbUsers", String.valueOf(users.length));
    }

    public static void mapBuilder(Model model){

        final String uri = adresse + "area/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Area[]> response = restTemplate.getForEntity(uri, Area[].class);
        Area[] areas = response.getBody();

        List<Area> listArea = Arrays.asList(areas);

        for (int i = 0; i < listArea.size(); i++){

            JSONObject obj = new JSONObject(listArea.get(i).getCoordinates());
            Coordinates coord = new Coordinates(obj.getDouble("x1") ,obj.getDouble("y1"),
                                             obj.getDouble("x2") ,obj.getDouble("y2"),
                                             obj.getDouble("x3") ,obj.getDouble("y3"),
                                             obj.getDouble("x4") ,obj.getDouble("y4"));

            listArea.get(i).setCoordinatesXY(coord);

//            int zoneID = listArea.get(i).getId();
//
//            final String uriCountUser = adresse + "user/area/" + zoneID;
//
//            String listCountUsers = restTemplate.getForObject(uriCountUser, String.class);

//            listArea.get(i).setCountUser(listCountUsers.length);

//            log.info(String.valueOf(listCountUsers));
        }

        model.addAttribute("listArea", listArea);
    }

    @RequestMapping(value = "/creationPointInteret", method = RequestMethod.POST)
    public String creationPointInteret(@RequestParam("creationPointInteret") String creationPointInteret, Model model){

        model.addAttribute("Area" , new Area());
        model.addAttribute("header" , "creationPointInteret");

        return "index";
    }

    @RequestMapping(value = "/creation", method = RequestMethod.POST)
    public String creation(Model model,
                         @RequestParam("name") String name,
                         @RequestParam("capacity") int capacity,
                         @RequestParam("listX") String listX,
                         @RequestParam("listY") String listY){


        String splitX[] = listX.split(",");
        List<String> X = Arrays.asList(splitX);

        String splitY[] = listY.split(",");
        List<String> Y = Arrays.asList(splitY);

        final String uri = adresse + "area?name=" + name + "&capacity=" + capacity +
                "&x1=" + X.get(0) +
                "&y1=" + Y.get(0) +
                "&x2=" + X.get(1) +
                "&y2=" + Y.get(1) +
                "&x3=" + X.get(2) +
                "&y3=" + Y.get(2) +
                "&x4=" + X.get(3) +
                "&y4=" + Y.get(3);

        List<String> concatXY = new ArrayList<>();
        concatXY.add(X.get(0));
        concatXY.add(Y.get(0));
        concatXY.add(X.get(1));
        concatXY.add(Y.get(1));
        concatXY.add(X.get(2));
        concatXY.add(Y.get(2));
        concatXY.add(X.get(3));
        concatXY.add(Y.get(3));

        Area a = new Area(capacity, concatXY.toString(), name);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(uri, a, String.class);

        mapBuilder(model);
        panelModel(model);
        model.addAttribute("response", response);
        model.addAttribute("header", "panel");
        return "index";

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

    public void userModel(Model model){

        final String uri = adresse + "user/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
        User[] users = response.getBody();
        listUser = Arrays.asList(users);

        int cpt = 0;
        for(User u : listUser){
            cpt++;
            u.setId(cpt);
        }

        model.addAttribute("listUser", listUser);
        model.addAttribute("User", new User());
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public String updateUser(@RequestParam("email") String email,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("active") String active,
                             HttpServletRequest request,
                             Model model) {

        String admin = request.getSession().getAttribute("user").toString();

        final String uri = adresse + "admin/user/update?emailAdmin=" + admin + "&email=" + email;

        User userToUpdate = new User(email, firstName, lastName, active);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(uri, userToUpdate);

        log.info("User updated");
        model.addAttribute("header", "utilisateurs");

        userModel(model);
        return "index";

    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String delete(@RequestParam("email") String email,  HttpServletRequest request, Model model){

        String user = request.getSession().getAttribute("user").toString();

        final String uri = adresse + "admin/user/delete?emailAdmin=" + user + "&emailUser=" + email;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(uri, User.class);

        model.addAttribute("header", "utilisateurs");
        userModel(model);
        return "index";
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

    public void AllMesssage(Model model, HttpServletRequest request){

        String user = request.getSession().getAttribute("user").toString();

        final String uri = adresse + "message/all?emailAdmin=" + user;

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Message[]> response =
                //restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<Message[]>(){});
                restTemplate.getForEntity(uri, Message[].class);
        Message[] messages = response.getBody();
        listMessage = Arrays.asList(messages);

        model.addAttribute("listMessage", listMessage);
    }
}
