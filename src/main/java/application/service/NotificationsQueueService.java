package application.service;

import application.entity.Event;
import application.entity.User;

import java.util.*;

import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationsQueueService {
    @Autowired
    UserService userService;

    /**
     * Multimap pour stocker les notifications et leurs destinataires
     */
    public HashMultimap<Event, Long> queueEvent = HashMultimap.create();

    private static final Logger log = LoggerFactory.getLogger(NotificationsQueueService.class);

    /**
     * Ajoute un utilisateur comme destinataire de l'évènement passé en paramètre
     * Créé l'entrée dans la map si l'event n'y est pas déjà
     * @param event : Evenement objet de la notification
     * @param user : Utilisateur à notifier
     */
    public synchronized void addNotificationEvent(Event event, User user) {
        if (userService != null) {
            User retrievedUser = this.userService.findUserByEmail(user.getEmail());
            if (retrievedUser != null) {
//                log.info("\t\t[#] User retrieved : " + retrievedUser);
                this.queueEvent.put(event, retrievedUser.getId());
            }
        }
//        log.info("\t\t[#] EventId : " + event.getId());
//        log.info("\t\t[#] Values Post Ajout : " + this.queueEvent.get(event));
    }

    /**
     * Récupère les notification concernant l'utilsateur passé en paramètre
     * @param user : utilisateur à notifier
     * @return List d'évènements (= notification)
     */
    public List<Event> getNotificationsEventByUser(User user) {
        ArrayList<Event> res = new ArrayList<>();
        User retrievedUser = this.userService.findUserByEmail(user.getEmail());
        if (retrievedUser != null) {
            Set<Event> eventSet = new HashSet<>(this.queueEvent.keySet());
            for (Event event : eventSet) {
//                log.info("\t[-] DANS LE FOR (getNotif)");
//                log.info("\t\t[#] EventId : " + event.getId());
                Long userId = retrievedUser.getId();
//                log.info("\t\t[#] UserId : " + userId);
//                log.info("\t\t[#] Values : " + this.queueEvent.get(event));
                if (this.queueEvent.get(event).contains(userId)) {
//                    log.info("\t\t[#] DANS LE IF");
                    res.add(event);
                    //retirer l'id de l'user de la map
                    // Si plus d'id pour la notif -> la notif est automatiquement supprimée
                    this.queueEvent.get(event).remove(userId);
                }
            }
        }
        return res;
    }

}
