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

    public HashMultimap<Event, Long> queueEvent = HashMultimap.create();

    private static final Logger log = LoggerFactory.getLogger(NotificationsQueueService.class);

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
