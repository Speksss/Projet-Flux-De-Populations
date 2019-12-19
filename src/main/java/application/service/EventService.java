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
     * Sauvegarde d'un événement en base de données et génère les notifications associées
     * @param event : l'événement à sauvegarder
     * @return L'événement crée, null sinon
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
     * Modification d'un événement en base de données
     * @param event : l'événement à modifier
     * @return L'événement modifié, null sinon
     */
    public Event updateEvent(Event event) {
//        log.info("updateEvent() : {}", event.toString());
        return eventRepository.save(event);
    }

    /**
     * Recherche d'un Evenement par son ID
     * @param id : id de l'événement à chercher
     * @return L'événement ou null sinon
     */
    public Event findEventById(long id) {
        return this.eventRepository.findById(id);
    }

    /**
     * Recherche des événements selon la zone ou ils se déroulent
     * @param area : La zone à explorer
     * @return La liste des événements ou null;
     */
    public List<Event> findEventsByArea(Area area) {
        return this.eventRepository.findByArea(area);
    }

    /**
     * Recherche les événements en fonction du type d'événement
     * @param eventType : Le type d'événement à utiliser
     * @return La liste des événements ou null;
     */
    public List<Event> findEventsByType(EventType eventType) {
        return this.eventRepository.findByEventType(eventType);
    }

    /**
     * Recherche les événement en fonction de la date
     * @param date Date de l'événement à trouver
     * @return La liste des événements (ou null)
     */
    public List<Event> findEventsByDate(Date date) {
        return this.eventRepository.findByDate(date);
    }

    /**
     * Recherche tous les événements
     * @return La liste des événements
     */
    public List<Event> findAllEvents() {
        return this.eventRepository.findAll();
    }

    /**
     * Recherche tous les événements actifs
     * @return La liste des événements
     */
    public List<Event> findAllActiveEvents() {
        return this.eventRepository.findAllByActiveIsTrue();
    }

    /**
     * Recherche tous les événements avec un nom de type d'événement donné
     * @param name : LE nom du type d'événement pour rechercher
     * @return LA liste de tous les événements
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


    /**
     * Supprime un evenement
     * @param e Evenement a supprimer
     */
    public void delete(Event e){
        this.eventRepository.delete(e);
    }

}
