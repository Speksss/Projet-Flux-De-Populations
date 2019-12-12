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
            return new ResponseEntity<>(event, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
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

    // Event Type

    @GetMapping("/event-type/all")
    @ResponseBody
    public ResponseEntity<List<EventType>> findAllEventType() {
        return new ResponseEntity<>(eventTypeService.findAllEventTypes(), HttpStatus.OK);
    }

    @PostMapping("/event-type/add")
    @ResponseBody
    public ResponseEntity<String> addEventType(@RequestParam("name") String name,
                                               @RequestParam("description") String description) {
        if(this.eventTypeService.findEventTypeByName(name) != null) {
            return new ResponseEntity<>("Ce nom est déjà utilisée par un autre type d'évènement.",
                    HttpStatus.NOT_MODIFIED);
        }
        EventType eventType = new EventType();
        eventType.setName(name);
        eventType.setDescription(description);
        if(this.eventTypeService.saveNewEventType(eventType) != null) {
            return new ResponseEntity<>("Le type d'évènement a correctement été créé.", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Une erreur est survenue lors de la création de votre type d'événement.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/event-type/edit")
    @ResponseBody
    public ResponseEntity<String> editEventType(@RequestParam("name") String name,
                                                @RequestParam("description") String description) {
        EventType eventType = null;
        if((eventType = this.eventTypeService.findEventTypeByName(name)) == null) {
            return new ResponseEntity<>("Ce type d'évènement n'existe pas.", HttpStatus.NOT_MODIFIED);
        }
        eventType.setDescription(description);
        if(this.eventTypeService.editEventType(eventType) != null) {
            return new ResponseEntity<>("Le type d'évènement a correctement été modifié.", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Une erreur est survenue lors de la modification de votre type d'événement.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/event-type/delete")
    @ResponseBody
    public ResponseEntity<String> deleteEventType(@RequestParam("name") String name) {
        EventType eventType = null;
        if((eventType = this.eventTypeService.findEventTypeByName(name)) == null) {
            return new ResponseEntity<>("Ce type d'évènement n'existe pas.", HttpStatus.NOT_MODIFIED);
        }
        if(this.eventTypeService.deleteEventType(eventType)) {
            return new ResponseEntity<>("Le type d'évènement a correctement été supprimé.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Une erreur est survenue lors de la suppression du type d'évènement",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
