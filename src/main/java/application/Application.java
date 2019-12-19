package application;

import application.entity.*;
import application.service.*;
import application.utils.Point;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Duration;
import java.util.Date;


import java.util.List;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
@EnableScheduling
public class Application {

    @Autowired
    UserLocationService userLocationService;
    @Autowired
    CapteurService capteurService;
    @Autowired
    EventService eventService;
    @Autowired
    AreaService areaService;
    @Autowired
    EventTypeService eventTypeService;

    @Autowired
    NotificationsQueueService notificationsQueueService;

    private static final Logger log = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Analyse le système pour la détection des évents toutes les 30 secondes
     */
    @Scheduled(fixedRate = 30000)
    public void analyse() {
       log.info("Détection des événements : analyse en cours");
       detectUsersOutOfZone();
       detectEventAffluence();
       analyseCapteurs();
       log.info("Détection des événements : analyse terminée");
    }

    /**
     * Traite les données réceptionnées depuis les capteurs
     */
    private void analyseCapteurs() {

        /*************************/
        /** Creation des events **/
        /*************************/

        for(Capteur c : capteurService.getAll()){
            JSONObject jobj = new JSONObject(c.getDatas());
            for(String nomCapteur : jobj.keySet()){
                JSONObject capteurData = jobj.getJSONObject(nomCapteur);
                double temperature = Double.MAX_VALUE;
                double luminance = Double.MAX_VALUE;
                double latitude = Double.MAX_VALUE;
                double longitude = Double.MAX_VALUE;
                try {
                    temperature = Double.parseDouble((String) capteurData.get("Temperature"));
                    luminance = Double.parseDouble((String) capteurData.get("Luminance"));
                    latitude = Double.parseDouble((String)capteurData.get("latitude"));
                    longitude = Double.parseDouble((String)capteurData.get("longitude"));
                    List<Area> listArea = areaService.findAreasByCoordinates(latitude,longitude);
                    Area a = null;
                    for(Area tmp : listArea){
                        if((a == null)||(tmp.getAreaArea()<a.getAreaArea()))a = tmp;
                    }


                    boolean newEvent = false;
                    EventType type = null;

                    if(temperature > 50.0) {//Temperature critique
                        type = eventTypeService.findEventTypeByName("TemperatureCritique");
                        newEvent = true;
                    }else if((temperature < 50.0)&&(temperature>30.0)){//Temperature haute
                        type = eventTypeService.findEventTypeByName("TemperatureElevee");
                        newEvent = true;
                    }else if(temperature < 15){
                        type = eventTypeService.findEventTypeByName("TemperatureFaible");
                        newEvent = true;
                    }

                    if(newEvent){
                        List<Event> eventList = eventService.findAllByAreaAndEventTypeAndActive(a,type,true);
                        Event event;
                        if(eventList.size() == 0){
                            //Création d'un évent
                            event = new Event();
                            event.setActive(true);
                            event.setDate(new Date());
                            event.setArea(a);
                            event.setEventType(type);
                            event.setName(type.getName());
                            log.info("["+type.getName()+"]"+a.getName()+" : "+nomCapteur);
                        }else{
                            event = eventList.get(0);
                            event.setDate(new Date());
                        }
                        eventService.saveEvent(event);
                    }
                }catch(JSONException e){
                    log.error("Erreur lors de la réception d'une ou plusieurs données.");
                }
            }
        }

        /***************************/
        /** Annulation des events **/
        /***************************/
        List<Event> eventList = eventService.findAllActiveEvents();

        for(Event e : eventList){
            EventType type = e.getEventType();
            //Annulation events Température*
            if((type.getName().equals("TemperatureCritique"))||(type.getName().equals("TemperatureElevee"))||(type.getName().equals("TemperatureFaible"))){
                boolean res = false;
                for(Capteur c : capteurService.getCapteurByArea(e.getArea())){
                    JSONObject jobj = new JSONObject(c.getDatas());
                    for(String nomCapteur : jobj.keySet()) {
                        double temperature = Double.parseDouble((String) jobj.getJSONObject(nomCapteur).get("Temperature"));
                        if(
                                ((type.getName().equals("TemperatureCritique"))&&(temperature>50.0)) ||
                                ((type.getName().equals("TemperatureElevee"))&&(temperature>30.0)&&(temperature < 50.0)) ||
                                ((type.getName().equals("TemperatureFaible"))&&(temperature<15.0))
                        ){
                            res = true;
                            break;
                        }
                    }
                    if(res)break;
                }

                if(!res){//Event plus d'actualité
                    e.setActive(false);
                    eventService.saveEvent(e);
                    log.info("[DESACTIVATION] Event :"+e.getName()+" in "+e.getArea().getName());
                }
            }
        }
    }


    /**
     * Détecte les utilisateurs en dehors de la zone principale (UPHF)
     */
    private void detectUsersOutOfZone() {
        log.info("[START] recherche des utilisateurs sur zone");
        List<UserLocation> userLocations = userLocationService.getAll();
        for(UserLocation ul : userLocations){
            Area uphf = new Area();
            uphf.setCoordinates("{\"x1\":\"50.320309\",\"y1\":\"3.513892\",\"x2\":\"50.329210\",\"y2\":\"3.518154\",\"x3\":\"50.328642\",\"y3\":\"3.509260\",\"x4\":\"50.321190\",\"y4\":\"3.508305\"}");
            if(((ul.getLongitude()==0)&&(ul.getLatitude()==0))||(!areaService.isPointInArea(uphf,new Point(ul.getLatitude(),ul.getLongitude())))){
                if(ul.isInZone()) {
                    ul.setInZone(false);
                    userLocationService.saveUserLocation(ul);
                }
            }else if(!ul.isInZone()){
                ul.setInZone(true);
                userLocationService.saveUserLocation(ul);
            }
        }
        log.info("[END] recherche des utilisateurs sur zone");
    }

    /**
     * Crée un événement selon l'affluence
     */
    public void detectEventAffluence(){
        log.info("[START] Analyse des positions utilisateurs");
        List<UserLocation> userLocations = userLocationService.getAllInZone();
        List<Area> areaList = areaService.findAllAreas();
        EventType type = eventTypeService.findEventTypeByName("Affluence");
        //Compte le nombre d'utilisateurs par zones
        for (Area a : areaList) {
            int count = 0;
            for (UserLocation uL : userLocations) {
                Point p = new Point(uL.getLatitude(),uL.getLongitude());
                if (this.areaService.isPointInArea(a, p)) {
                    count ++;
                }
            }
            List<Event> eventList = eventService.findAllByAreaAndEventTypeAndActive(a,type,true);
            //Vérification du dépassement de capacité (= event Affluence)
            if(count > a.getCapacity()){
                //Si il n'y a pas déjà un event en cours
                if(eventList.size()==0){
                    log.info("[AFFLUENCE]"+a.getName()+" : "+count+"/"+a.getCapacity());
                    //Création d'un évent
                    Event event = new Event();
                    event.setActive(true);
                    event.setDate(new Date());
                    event.setArea(a);
                    event.setEventType(type);
                    event.setName(type.getName());
                    eventService.saveEvent(event);
                }else{
                    //Mise a jour de la date
                    Event event = eventList.get(0);
                    event.setDate(new Date());
                    eventService.updateEvent(event);
                }
            }else {
                //Desactivation des event après 15 minutes
                if (eventList.size() > 0) {
                    for (Event e : eventList) {
                        DateTime eventDate = new DateTime(e.getDate());
                        Interval interval = new Interval(eventDate, new DateTime());
                        if(interval.toDuration().getStandardMinutes() >= 15){
                            e.setActive(false);
                            eventService.updateEvent(e);
                            log.info("[DESACTIVATION EVENT] "+e.getName()+" : "+e.getArea().getName());
                        }
                    }
                }
            }
        }
        log.info("[FIN] Analyse des positions utilisateurs");
    }
}



