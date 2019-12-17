package application;

import application.entity.Area;
import application.entity.Event;
import application.entity.EventType;
import application.entity.UserLocation;
import application.service.*;
import application.utils.Point;
import org.joda.time.DateTime;
import org.joda.time.Interval;
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
        List<UserLocation> userLocations = userLocationService.getAllNotNull();
        List<Area> areaList = areaService.findAllAreas();
        //Compte le nombre d'utilisateurs par zones
        for (Area a : areaList) {
            int count = 0;
            for (UserLocation uL : userLocations) {
                Point p = new Point(uL.getLatitude(),uL.getLongitude());
                if (this.areaService.isPointInArea(a, p)) count ++;
            }

            EventType type = eventTypeService.findEventTypeByName("Influence");
            List<Event> eventList = eventService.findAllByAreaAndEventTypeAndActive(a,type,true);

            //Vérification du dépassement de capacité (= event Influence)
            if(count > a.getCapacity()){
                //Si il n'y a pas déjà un event en cours
                if(eventList.size()==0){
                    log.info("[INFLUENCE]"+a.getName()+" : "+count+"/"+a.getCapacity());
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
                    eventService.saveEvent(event);
                }
            }else {
                //Desactivation des event après 15 minutes
                if (eventList.size() > 0) {
                    for (Event e : eventList) {
                        DateTime eventDate = new DateTime(e.getDate());
                        Interval interval = new Interval(eventDate, new DateTime());
                        if(interval.toDuration().getStandardMinutes() >= 15){
                            e.setActive(false);
                            eventService.saveEvent(e);
                            log.info("[DESACTIVATION EVENT] "+e.getName()+" : "+e.getArea().getName());
                        }
                    }
                }
            }
        }
        log.info("Détection des événements : analyse terminée");
    }
}



