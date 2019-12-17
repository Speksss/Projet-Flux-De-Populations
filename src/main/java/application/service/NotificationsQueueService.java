package application.service;

import application.entity.User;

import java.util.*;

import application.utils.Notification;
import com.google.common.collect.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationsQueueService {
    @Autowired
    UserService userService;

    public SortedSetMultimap<Notification, Long> queue;

    public NotificationsQueueService() {
        this.queue = TreeMultimap.create();
    }

    public synchronized void addNotification(Notification notification, User user) {
        if (userService != null) {
            User retrievedUser = this.userService.findUserByEmail(user.getEmail());
            if (retrievedUser != null) {
                this.queue.put(notification, retrievedUser.getId());
            }
        }
    }

    public List<Notification> getNotificationsByUser(User user) {
        ArrayList<Notification> res = new ArrayList<>();
        User retrievedUser = this.userService.findUserByEmail(user.getEmail());
        if (retrievedUser != null) {
            Set<Notification> notificationSet = new HashSet<>(this.queue.keySet());
            for (Notification notif : notificationSet) {
                Long userId = retrievedUser.getId();
                if (this.queue.get(notif).contains(userId)) {
                    res.add(notif);
                    //retirer l'id de l'user de la map
                    // Si plus d'id pour la notif -> la notif est automatiquement supprimée
                    this.queue.get(notif).remove(userId);
                }
            }
        }
        return res;
    }
}
