package application.controller;

import application.entity.EventType;
import application.entity.Subscription;
import application.entity.User;
import application.service.EventTypeService;
import application.service.SubscriptionService;
import application.service.UserService;
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
     * @return (the list of all subscriptions)
     */
    @GetMapping("/subscription/all")
    @ResponseBody
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return new ResponseEntity<>(this.subscriptionService.findAllSubscriptions(), HttpStatus.OK);
    }

    /**
     * @param email (to find the user)
     * @param name (to find the event type)
     */

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
     * @param email (The list of events types to which a user has subscribed)
     *
     * @return
     */
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
     * @param name (delete a subscription by event type)
     *
     * @return
     */
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
     * @param email (to find the user)
     * @param name (delete the user from the list of subscribers to this event type)
     *
     * @return
     */
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