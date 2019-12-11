package application.controller;

import application.entity.Event;
import application.entity.EventType;
import application.service.EventService;
import application.service.EventTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private EventTypeService eventTypeService;

    @PostMapping("/event/raise")
    @ResponseBody
    public ResponseEntity<Event> raise(@RequestParam("name") String name, @RequestParam("date") String date,
                                       @RequestParam("latitude") String latitude,
                                       @RequestParam("longitude") String longitude) {
        log.debug("raise {}", date);
        log.debug("raise {}", latitude);
        log.debug("raise {}", longitude);
        Event     event = new Event();
        EventType type  = eventTypeService.findEventTypeByName(name);
        if(type != null) {
            event.setActive(true);
            event.setDate(null);
            //            Location location = new Location();
            //            location.setLatitude(Double.parseDouble(latitude));
            //            location.setLongitude(Double.parseDouble(longitude));
            event.setArea(null);
            event.setEventType(type);
            event.setName(type.getName());
            eventService.saveNewEvent(event);
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/event/all")
    @ResponseBody
    public ResponseEntity<List<Event>> findAll() {
        return new ResponseEntity<>(eventService.findAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/event/all/actives")
    @ResponseBody
    public ResponseEntity<List<Event>> findAllActives() {
        return new ResponseEntity<>(eventService.findAllActiveEvents(), HttpStatus.OK);
    }

}
