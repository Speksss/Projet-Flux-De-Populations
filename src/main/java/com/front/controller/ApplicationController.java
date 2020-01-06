package com.front.controller;

import com.front.Main;
import com.front.entity.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    public String header(@RequestParam("select") String selectHeader,
                         @RequestParam(name = "sensorId", required = false) String sensorId,
                         @RequestParam(name = "eventName", required = false) String eventName,
                         @RequestParam(name = "selectEventActive", required = false) String selectEventActive,
                         @RequestParam(name = "selectEventType", required = false) String selectEventType,
                         @RequestParam(name = "fromDate", required = false) String fromDate,
                         @RequestParam(name = "toDate", required = false) String toDate,
                         Model model, HttpServletRequest request) throws ParseException{


        if(checkSessionTokenValidity(request)){
            if(selectHeader.equals("panel")){ panelModel(model); mapBuilder(model); model.addAttribute("header", "panel");}
            if(selectHeader.equals("capteurs")){ sensorsModel(model); model.addAttribute("header", "capteurs");}
            if(selectHeader.equals("capteur")){ sensorModel(model,sensorId); model.addAttribute("header", "capteur");}
            if(selectHeader.equals("utilisateurs")){ userModel(model); model.addAttribute("header", "utilisateurs");}
            if(selectHeader.equals("evenements")){ eventModel(model, eventName, selectEventActive, selectEventType, fromDate, toDate); model.addAttribute("header", "evenements");}
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

    public void eventModel(Model model,
                           String eventName,
                           String selectEventActive,
                           String selectEventType,
                           String fromDate,
                           String toDate) throws ParseException{

        String uriEvent;
        long dateFromFilter  = 0;
        long dateToFilter  = 0;
        SimpleDateFormat formatterToShowFilter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");	// Format que l'on récupère du formulaire
        String fromDateFormatted = "";
        String toDateFormatted = "";

        if(eventName==null && selectEventActive==null && selectEventType==null && fromDate==null && toDate==null) {
            uriEvent = adresse + "event/all";
        }
        else {
            if(selectEventActive==null){selectEventActive="";}
            if(selectEventType==null){selectEventType="";}
            if(fromDate!=""){
                Date fromDateParse = formatter.parse(fromDate);						// On convertit en Date
                dateFromFilter = fromDateParse.getTime();
                fromDateFormatted = formatterToShowFilter.format(fromDateParse);	// Le format à afficher sur la page
            }

            if(toDate!=""){
                Date toDateParse = formatter.parse(toDate);							// On convertit en Date
                dateToFilter = toDateParse.getTime();
                toDateFormatted = formatterToShowFilter.format(toDateParse);		// Le format à afficher sur la page
            }

            // si pas de date max -> on ne le passe pas dans la requête
            if(dateToFilter==0) {
                uriEvent = adresse + "event/all/filters?areaName=" + eventName + "&active=" + selectEventActive + "&typeName=" + selectEventType + "&dateFrom=" + dateFromFilter ;
            }
            else {
                uriEvent = adresse + "event/all/filters?areaName=" + eventName + "&active=" + selectEventActive + "&typeName=" + selectEventType + "&dateFrom=" + dateFromFilter + "&dateTo=" + dateToFilter ;
            }
        }

        //log.info(uriEvent);

        final String uri = uriEvent;

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Event[]> response = restTemplate.getForEntity(uri, Event[].class);
        Event[] events = response.getBody();

        listEvent = Arrays.asList(events);

        for(Event e : listEvent){
            e.setId(e.getId()-14);
        }

        model.addAttribute("listEvent", listEvent);
        model.addAttribute("eventName", eventName);
        model.addAttribute("selectEventActive", selectEventActive);
        model.addAttribute("selectEventType", selectEventType);
        model.addAttribute("fromDateFormatted", fromDateFormatted);
        model.addAttribute("toDateFormatted", toDateFormatted);

        eventTypeModel(model); 		//Pour afficher la liste des types dans le select du filtre
    }

    public void eventTypeModel(Model model) {
        final String uri = adresse + "/event-type/all";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<EventType[]> response = restTemplate.getForEntity(uri, EventType[].class);
        EventType[] eventTypeList = response.getBody();

        model.addAttribute("eventTypeList", eventTypeList);
    }

    public void userModel(Model model){

        listUser.clear(); // Nettoyage de la liste

        final String uri = adresse + "user/all";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
        User[] users = response.getBody();

        int cpt = 0;
        for(User u : users){
            if(!u.getRoles().contains("ROLE_ADMIN") && u.getRoles().size() != 0){
                cpt++;
                u.setId(cpt);
                listUser.add(u);
            }
        }

        model.addAttribute("listUser", listUser);
        model.addAttribute("User", new User());
    }

    public void sensorModel(Model model, String sensorId) {
        //TODO En attente de l'API, récupération des données d'un capteur,


    	/*final String uri ="http://35.206.157.216:8080/capteur?id=" + sensorId;
    	RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Capteur> response = restTemplate.getForEntity(uri, Capteur.class);
        Capteur capteur = response.getBody();
    	model.addAttribute("capteur", capteur);*/
        model.addAttribute("capteur", sensorId);	// uniquement avec les données brutes, à supprimer lorsqu'on récuperera les données

    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public String updateUser(@RequestParam("email") String email,
                             @RequestParam("firstName") String newFirstName,
                             @RequestParam("lastName") String newLastName,
                             @RequestParam("active") boolean newActive,
                             HttpServletRequest request,
                             Model model) {

        User userToUpdate = null;
        for (User u : listUser) {
          if(u.getEmail().equals(email)){
              userToUpdate = u;
          }
        }

        String admin = request.getSession().getAttribute("user").toString();
        String uri = adresse + "admin/user/update?emailAdmin=" + admin + "&email=" + email;

        if(newFirstName != null && !newFirstName.equals("")){
            uri += "&firstName=" + newFirstName;
            userToUpdate.setFirstName(newFirstName);
        }
        if(newLastName != null && !newLastName.equals("")){
            uri += "&lastName=" + newLastName;
            userToUpdate.setLastName(newLastName);
        }
        uri += "&isActive=" + newActive;
        userToUpdate.setActive(newActive);

        log.info(email);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(uri, userToUpdate, String.class);

        log.info(response);

        userModel(model);
        model.addAttribute("header", "utilisateurs");
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

    public void sensorsModel(Model model) {

        final String uri = adresse +"capteurs";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Capteur[]> response = restTemplate.getForEntity(uri, Capteur[].class);
        Capteur[] capteurs = response.getBody();
        model.addAttribute("capteurs", capteurs);

        Map<String,Datas> capteursDatas = new HashMap<>();
        for(Capteur capteur : capteurs) {

            String json = capteur.getDatas();
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            for (String key: jsonObject.keySet()){

                JsonObject jsonObjectDatas = new JsonParser().parse(jsonObject.get(key).toString()).getAsJsonObject();

                String idSensor = capteur.getId();
                String longitude = jsonObjectDatas.get("longitude").toString().substring(1,jsonObjectDatas.get("longitude").toString().length()-1) ;
                String latitude = jsonObjectDatas.get("latitude").toString().substring(1,jsonObjectDatas.get("latitude").toString().length()-1) ;
                String batteryLevel;
                try{
                    batteryLevel = jsonObjectDatas.get("Battery Level").toString().substring(1,jsonObjectDatas.get("Battery Level").toString().length()-1) ;
                }
                catch(Exception NullPointerException){
                    batteryLevel = "";
                }

                Datas sensorDatas = new Datas(idSensor,latitude,longitude,batteryLevel);
                capteursDatas.put(key,sensorDatas);
            }
        }
        model.addAttribute("capteursDatas", capteursDatas);
    }

    public boolean checkSessionTokenValidity(HttpServletRequest request) {
        return (request.getSession().getAttribute("user") != null);
    }

    public void showOneSensor(Model model, String sensorId) {

        final String uri = adresse +"capteur?id=" + sensorId;
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Capteur> response = restTemplate.getForEntity(uri, Capteur.class);
        Capteur capteur = response.getBody();
        String json = capteur.getDatas();
        Object sensorDatas = null;
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        for (String key: jsonObject.keySet()){

            JsonObject jsonObjectDatas = new JsonParser().parse(jsonObject.get(key).toString()).getAsJsonObject();

            String idSensor = capteur.getId();
            String sensorName = key;
            String luminance = jsonObjectDatas.get("Luminance").toString().substring(1,jsonObjectDatas.get("Luminance").toString().length()-1) ;
            String temperature = jsonObjectDatas.get("Temperature").toString().substring(1,jsonObjectDatas.get("Temperature").toString().length()-1);
            String longitude = jsonObjectDatas.get("longitude").toString().substring(1,jsonObjectDatas.get("longitude").toString().length()-1) ;
            String latitude = jsonObjectDatas.get("latitude").toString().substring(1,jsonObjectDatas.get("latitude").toString().length()-1) ;
            String batteryLevel;
            try{
                batteryLevel = jsonObjectDatas.get("Battery Level").toString().substring(1,jsonObjectDatas.get("Battery Level").toString().length()-1) ;
            }
            catch(Exception NullPointerException){
                batteryLevel = "";
            }

            sensorDatas = new Datas(idSensor,latitude,longitude,temperature,luminance,batteryLevel,sensorName);

        }


        model.addAttribute("capteur", sensorDatas);
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
