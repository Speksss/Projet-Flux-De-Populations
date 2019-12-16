package application.controller;

import application.entity.Event;
import application.entity.EventType;
import application.service.EventService;
import application.service.EventTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlleur dédié a la manipulation des evenements
 */
@RestController
@Api(value = "fluxDePopulation", description = "Controller pour gérer les levés d'évènements et la création de type " +
        "d'évènement.", produces = "application/json")
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
            eventService.saveEvent(event);
            return new ResponseEntity<>(event, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/event/all")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des évènements en base")
    public ResponseEntity<List<Event>> findAll() {
        return new ResponseEntity<>(eventService.findAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/event/all/filters")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des évènements en base en utilisant les filtres")
    public ResponseEntity<List<Event>> findAllFiltered(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "typeName", required = false) String typeName,
                                               @RequestParam(value = "active", required = false) Boolean active,
                                               @RequestParam(value = "areaName", required = false) String areaName,
                                               @RequestParam(value = "dateFrom", required = false) Long dateFrom,
                                               @RequestParam(value = "dateTo", required = false) Long dateTo) {
        List<Event> events = eventService.findAllEvents();

        if(name != null && !name.equals(""))
            events.removeIf(event -> !event.getName().equals(name));
        if(typeName != null && !typeName.equals(""))
            events.removeIf(event -> event.getEventType() == null || !event.getEventType().getName().equals(typeName));
        if(active != null)
            events.removeIf(event -> event.isActive() != active);
        if(areaName != null && !areaName.equals(""))
            events.removeIf(event -> event.getArea() == null || !event.getArea().getName().equals(areaName));
        if(dateFrom != null)
            events.removeIf(event -> event.getDate() == null || event.getDate().getTime() < dateFrom);
        if(dateTo != null)
            events.removeIf(event -> event.getDate() == null || event.getDate().getTime() > dateTo);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/event/all/actives")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des évènements actifs en base")
    public ResponseEntity<List<Event>> findAllActives() {
        return new ResponseEntity<>(eventService.findAllActiveEvents(), HttpStatus.OK);
    }

    @GetMapping("/event/all/type-name")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des évènements selon leur type")
    public ResponseEntity<List<Event>> findAllByEventType(@RequestParam("name") String name) {
        return new ResponseEntity<>(eventService.findAllEventByTypeName(name), HttpStatus.OK);
    }

    // Event Type

    @GetMapping("/event-type/all")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des type d'évènements possibles en base")
    public ResponseEntity<List<EventType>> findAllEventType() {
        return new ResponseEntity<>(eventTypeService.findAllEventTypes(), HttpStatus.OK);
    }

    @PostMapping("/event-type/add")
    @ResponseBody
    @ApiOperation(value = "Permet la création d'un type d'évènement possible")
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
    @ApiOperation(value = "Permet l'édition d'un type d'évènement possible")
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

    @DeleteMapping("/event-type/delete")
    @ResponseBody
    @ApiOperation(value = "Permet la suppression d'un type d'évènement possible")
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
