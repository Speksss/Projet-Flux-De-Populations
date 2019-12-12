package application.controller;

import application.entity.EventType;
import application.entity.Subscriptions;
import application.entity.User;
import application.service.EventTypeService;
import application.service.SubscriptionsService;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author backTeam
 * Subscription Ccntroller
 */
@RestController
public class SubscriptionsController {
	
	@Autowired
	SubscriptionsService subscriptionsService;
	
	@Autowired
	UserService userService;
	
	@Autowired
    private EventTypeService eventTypeService;
	/**
	 * @return la liste de tous les abonnements
	 */
	@GetMapping("/subscriptions/all")
	@ResponseBody
	public ResponseEntity<List<Subscriptions>> getAllSubscriptions() {
		return new ResponseEntity<>(this.subscriptionsService.findAllSubscriptions(), HttpStatus.OK);
	}

	@PostMapping("/subscriptions/register")
	    @ResponseBody
	    public ResponseEntity<String> register(@RequestParam("email") String email,
	                                           @RequestParam("name") String name) {
		 
		       
	        User user = userService.findUserByEmail(email);
	        EventType eventType = eventTypeService.findEventTypeByName(name);
	        	               
	        if (user != null && eventType != null) {
	        	Subscriptions inSubscription = subscriptionsService.findSubscriptionsByType(eventType);
	        	if(inSubscription != null) {
	        		inSubscription.getUsers().add(user);
	        		subscriptionsService.saveSubcribe(inSubscription);
	        		return new ResponseEntity<>("abonnement Réussi.", HttpStatus.ACCEPTED);
	        	}else {
	        		Subscriptions subscription = new Subscriptions();
	        		subscription.setEventType(eventType);
		        	subscription.getUsers().add(user);
		        	subscriptionsService.saveSubcribe(subscription);
		            return new ResponseEntity<>("abonnement Réussi.", HttpStatus.CREATED);
	        	}
	        }
	        return new ResponseEntity<>("Une erreur est survenue lors de votre abonnement.", HttpStatus.NOT_MODIFIED);
	        
	    }
	
	
}
