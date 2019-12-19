package application.service;

import application.entity.*;
import application.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Services liés aux evenements
 */
@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    SubscriptionService subscriptionService;
    
    @Autowired
    NotificationsQueueService notificationsQueueService;

    /**
     * Sauvegarde d'un évènement en base de données et génère les notifications associées
     * @param event : l'évènement à sauvegarder
     * @return L'évènement crée, null sinon
     */
    public Event saveEvent(Event event) {
//        log.info("[*] saveEvent() : {}", event.toString());

        Event savedEvent = eventRepository.save(event);

        if (savedEvent != null) {
            // Création des notifications associées
            Subscription subscription = subscriptionService.findSubscriptionByType(savedEvent.getEventType());
            if (subscription != null) {
                for (User user : subscription.getUsers()) {
//                    log.info("\t[-] DANS LE FOR (saveEvent)");
                    notificationsQueueService.addNotificationEvent(savedEvent, user);
                }
            }
        }

        return savedEvent;
    }

    /**
     * Modification d'un évènement en base de données
     * @param event : l'évènement à modifier
     * @return L'évènement modifié, null sinon
     */
    public Event updateEvent(Event event) {
//        log.info("updateEvent() : {}", event.toString());
        return eventRepository.save(event);
    }

    /**
     * Recherche d'un Evenement par son ID
     * @param id : id de l'évènement à chercher
     * @return L'évènement ou null sinon
     */
    public Event findEventById(long id) {
        return this.eventRepository.findById(id);
    }

    /**
     * Recherche des évènements selon la zone ou ils se déroulent
     * @param area : La zone à explorer
     * @return La liste des évènements ou null;
     */
    public List<Event> findEventsByArea(Area area) {
        return this.eventRepository.findByArea(area);
    }

    /**
     * Recherche les évènements en fonction du type d'évènement
     * @param eventType : Le type d'évènement à utiliser
     * @return La liste des évènements ou null;
     */
    public List<Event> findEventsByType(EventType eventType) {
        return this.eventRepository.findByEventType(eventType);
    }

    public List<Event> findEventsByDate(Date date) {
        return this.eventRepository.findByDate(date);
    }

    /**
     * Recherche tous les évènements
     * @return La liste des évènements
     */
    public List<Event> findAllEvents() {
        return this.eventRepository.findAll();
    }

    /**
     * Recherche tous les évènements actifs
     * @return La liste des évènements
     */
    public List<Event> findAllActiveEvents() {
        return this.eventRepository.findAllByActiveIsTrue();
    }

    /**
     * Recherche tous les évènements avec un nom de type d'évènement donné
     * @param name : LE nom du type d'évènement pour rechercher
     * @return LA liste de tous les évènements
     */
    public List<Event> findAllEventByTypeName(String name) {
        return this.eventRepository.findAllByEventType_Name(name);
    }

    /**
     * Trouve les événements en fonction d'une zone, de son type d'événement, et s'il est actif ou non
     * @param a Zone de l'événement à trouver
     * @param et Type d'événement de l'événement à trouver
     * @param b Si l'événement à trouver est actif ou non
     * @return Une liste d'événements
     */
    public List<Event> findAllByAreaAndEventTypeAndActive(Area a,EventType et,boolean b){
        return this.eventRepository.findAllByAreaAndEventTypeAndActive(a,et,b);
    }
}
