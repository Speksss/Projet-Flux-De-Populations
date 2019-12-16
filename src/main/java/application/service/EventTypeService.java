package application.service;

import application.entity.Event;
import application.entity.EventType;
import application.repository.EventTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Services liés aux types d'evenements
 */
@Service
public class EventTypeService {

    private static final Logger log = LoggerFactory.getLogger(EventTypeService.class);

    @Autowired
    private EventTypeRepository eventTypeRepository;

    /**
     * Sauvegarde d'un nouveau type d'evenement
     * @param eventType : le type d'evenement à sauvegarder
     * @return Le type d'evenement cree, null sinon
     */
    public EventType saveNewEventType(EventType eventType) {
        log.info("save New EventType() : {}", eventType.toString());
        return this.eventTypeRepository.save(eventType);
    }

    /**
     * Edite un type d'evenement
     * @param eventType : le type d'evenement à editer
     * @return Le type d'evenement modifie, null sinon
     */
    public EventType editEventType(EventType eventType) {
        log.info("edit EventType() : {}", eventType.toString());
        return this.eventTypeRepository.save(eventType);
    }


    /**
     * Supprime un type d'evenement
     * @param eventType : le type d'evenement à supprimer
     * @return True si le type d'evenement a bien ete supprime, false sinon
     */
    public boolean deleteEventType(EventType eventType) {
        log.info("delete EventType() : {}", eventType.toString());
        this.eventTypeRepository.delete(eventType);
        return this.eventTypeRepository.findByName(eventType.getName()) == null;
    }

    /**
     *  Recherche un type d'evenement par son id
     * @param id : id du type d'evenement à trouver
     * @return Le type d'evenement cherché, ou null
     */
    public EventType findEventTypeById(int id) {
        return this.eventTypeRepository.findById(id);
    }

    /**
     * Recherche un type d'evenement par son nom
     * @param name : nom du type d'evenement à trouver
     * @return Le type d'evenement cherché, ou null
     */
    public EventType findEventTypeByName(String name) {
        return this.eventTypeRepository.findByName(name);
    }

    /**
     * Recherche tous les types d'evenement
     * @return La liste de tous les types d'evenement
     */
    public List<EventType> findAllEventTypes() {
        return this.eventTypeRepository.findAll();
    }

}