package application.controller;

import application.entity.Area;
import application.entity.Event;
import application.entity.EventType;
import application.service.AreaService;
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

import java.util.Date;
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

    @Autowired
    private AreaService areaService;

    @PostMapping("/event/raise")
    @ResponseBody
    public ResponseEntity<Event> raise(@RequestParam("name") String name, @RequestParam("date") String date,
                                       @RequestParam("latitude") Double latitude,
                                       @RequestParam("longitude") Double longitude) {
        log.debug("raise {}", date);
        log.debug("raise {}", latitude);
        log.debug("raise {}", longitude);
        Event     event = new Event();
        EventType type  = eventTypeService.findEventTypeByName(name);
        if(type != null) {
            event.setActive(true);
            event.setDate(new Date(Long.parseLong(date)));
            List<Area> areaList = areaService.findAreasByCoordinates(latitude, longitude);
            Area tmp = areaList.get(0);
            // On prend la zone la plus précise i.e. la zone avec l'aire la plus faible
            for (int i = 1; i < areaList.size(); i++) {
                if (areaList.get(i).getAreaArea() < tmp.getAreaArea()) {
                    tmp = areaList.get(i);
                }
            }
            event.setArea(tmp);
            event.setEventType(type);
            event.setName(type.getName());
            eventService.saveEvent(event);
            return new ResponseEntity<>(event, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Retourne la liste de tous les événements
     * @return La liste de tous les événements / http status ok
     */
    @GetMapping("/event/all")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des évènements en base")
    public ResponseEntity<List<Event>> findAll() {
        return new ResponseEntity<>(eventService.findAllEvents(), HttpStatus.OK);
    }

    /**
     * Trouve une liste d'événements en fonction de filtres s'appliquant sur des paramètres donnés
     * @param name Nom de l'événement
     * @param typeName Nom du type d'événement
     * @param active Si l'événement est actif ou non
     * @param areaName Nom de la zone
     * @param dateFrom Date à partir de laquelle filtrer les événements
     * @param dateTo Date jusqu'à laquelle filtrer les événements
     * @return Une liste filtrée des événements
     */
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

    /**
     * Trouve les événements actifs
     * @return La liste des événements actifs
     */
    @GetMapping("/event/all/actives")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des évènements actifs en base")
    public ResponseEntity<List<Event>> findAllActives() {
        return new ResponseEntity<>(eventService.findAllActiveEvents(), HttpStatus.OK);
    }

    /**
     * Trouve les événements selon un type d'événement donné
     * @param name Nom du type d'événement recherché
     * @return La liste filtrée des événements selon le type d'événement
     */
    @GetMapping("/event/all/type-name")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des évènements selon leur type")
    public ResponseEntity<List<Event>> findAllByEventType(@RequestParam("name") String name) {
        return new ResponseEntity<>(eventService.findAllEventByTypeName(name), HttpStatus.OK);
    }

    // Event Type

    /**
     * Renvoie tous les types d'événements existants
     * @return La liste des types d'événements existants
     */
    @GetMapping("/event-type/all")
    @ResponseBody
    @ApiOperation(value = "Affiche l'intégralité des type d'évènements possibles en base")
    public ResponseEntity<List<EventType>> findAllEventType() {
        return new ResponseEntity<>(eventTypeService.findAllEventTypes(), HttpStatus.OK);
    }

    /**
     * Ajoute un type d'événement en bdd
     * @param name Nom du type d'événement
     * @param description Description du type d'événement
     * @return Une réponse donnant des indications sur le déroulement de l'ajout / http status
     */
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

    /**
     * Edite un type d'événement se trouvant en base
     * @param name Nom du type d'événement
     * @param description Description du type d'événement
     * @return Une réponse donnant des indications sur le déroulement de la modification / http status
     */
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

    /**
     * Supprime un type d'événement
     * @param name Nom du type d'événement à supprimer
     * @return Une réponse donnant des indications sur le déroulement de la suppression / http status
     */
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
