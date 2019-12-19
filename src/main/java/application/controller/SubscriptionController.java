package application.controller;

import application.entity.EventType;
import application.entity.Subscription;
import application.entity.User;
import application.service.EventTypeService;
import application.service.SubscriptionService;
import application.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controlleur dédié a la gestion des abonnements
 */
@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventTypeService eventTypeService;

    /**
     * Retourne la liste des abonnements
     * @return (the list of all subscriptions)
     */
    @ApiOperation(value = "Retourne la liste de tous les abonnements")
    @GetMapping("/subscription/all")
    @ResponseBody
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return new ResponseEntity<>(this.subscriptionService.findAllSubscriptions(), HttpStatus.OK);
    }

    /**
     * Ajoute un utilisateur à un abonnement indiqué selon un type d'evenement
     * @param email (to find the user)
     * @param name (to find the event type)
     * @return http status / indication de comment s'est déroulé l'abonnement
     */

    @ApiOperation(value = "Ajoute un utilisateur à un abonnement")
    @PostMapping("/subscription/subscribe")
    @ResponseBody
    public ResponseEntity<String> subscribe(@RequestParam("email") String email, @RequestParam("name") String name) {
        User      user      = userService.findUserByEmail(email);
        EventType eventType = eventTypeService.findEventTypeByName(name);

        if(user != null && eventType != null) {
            Subscription subscription = subscriptionService.findSubscriptionByType(eventType);
            if(subscription != null) {
                subscription.getUsers().add(user);
                subscriptionService.saveSubscription(subscription);
                return new ResponseEntity<>("Subscription Successful.", HttpStatus.ACCEPTED);
            } else {
                subscription = new Subscription();
                subscription.setEventType(eventType);
                // Creating an empty Set
                Set<User> u = new HashSet<User>();
                u.add(user);
                subscription.setUsers(u);
                subscriptionService.saveSubscription(subscription);
                return new ResponseEntity<>("Subscription Successful.", HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>("An error occurred during your subscription.", HttpStatus.NOT_FOUND);
    }

    /**
     * Recupère la liste des types d'evenements des abonnements d'un utilisateur
     * @param email (pour trouver un utilisateur et récupérer ses abonnements)
     *
     * @return la liste des types d'evenements des abonnements de l'utilisateur donné et/ou un http status
     */
    @ApiOperation(value = "Retourne les abonnements d'un utilisateur")
    @GetMapping("/subscription/my-subscriptions")
    @ResponseBody
    public ResponseEntity<List<EventType>> userSubscriptions(@RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);

        if(user != null) {
            List<Subscription> subscriptions = subscriptionService.findAllSubscriptionsByUser(user);
            List<EventType>    eventTypes    = new ArrayList<>();
            subscriptions.forEach(subscription -> eventTypes.add(subscription.getEventType()));
            return new ResponseEntity<>(eventTypes, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Supprime un abonnement existant (Les utilisateurs ne pourront plus utiliser cet abonnement)
     * @param name (nom de l'abonnement à supprimer)
     *
     * @return http status / indication de comment s'est déroulé la suppression de l'abonnement
     */
    @ApiOperation(value = "Supprime un abonnement")
    @PostMapping("/subscription/delete")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestParam("name") String name) {
        EventType eventType = eventTypeService.findEventTypeByName(name);
        if(eventType != null) {
            Subscription inSubscription = subscriptionService.findSubscriptionByType(eventType);
            if(subscriptionService.deleteSubscription(inSubscription)) {
                return new ResponseEntity<>("successfully deleted.", HttpStatus.ACCEPTED);
            } else return new ResponseEntity<>("unsuccessful deletion.", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>("An error occurred during your operation.", HttpStatus.NOT_FOUND);
    }

    /**
     * Retire un utilisateur d'un abonnement donné en entrer
     * @param email (nom de l'utilisateur pour récupérer ses abonnements)
     * @param name (nom de l'abonnement auquel l'utilisateur va se désabonner)
     *
     * @return http status / indication de comment s'est déroulé le désabonnement
     */
    @ApiOperation(value = "Retire un utilisateur d'un abonnement donné en entrer")
    @PostMapping("/subscription/unsubscribe")
    @ResponseBody
    public ResponseEntity<String> unsubscribe(@RequestParam("email") String email, @RequestParam("name") String name) {
        User      user      = userService.findUserByEmail(email);
        EventType eventType = eventTypeService.findEventTypeByName(name);

        if(user != null && eventType != null) {
            Subscription subscription = subscriptionService.findSubscriptionByType(eventType);
            if(subscription != null && subscription.getUsers() != null) {
                if(subscription.getUsers().remove(user)) {
                    subscriptionService.saveSubscription(subscription);
                    return new ResponseEntity<>("Success: You have successfully unsubscribed.", HttpStatus.ACCEPTED);
                }
                return new ResponseEntity<>("No change.", HttpStatus.NOT_MODIFIED);
            }
        }
        return new ResponseEntity<>("An error occurred during your operation.", HttpStatus.NOT_FOUND);
    }

}