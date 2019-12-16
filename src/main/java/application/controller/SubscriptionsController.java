package application.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import application.entity.EventType;
import application.entity.Subscriptions;
import application.entity.User;
import application.service.EventTypeService;
import application.service.SubscriptionsService;
import application.service.UserService;

/**
 * Controlleur dédié a la gestion des abonnements
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
	 * @return (the list of all subscriptions)
	 */
	@GetMapping("/Subscriptions/all")
	@ResponseBody
	public ResponseEntity<List<Subscriptions>> getAllSubscriptions() {
		return new ResponseEntity<>(this.subscriptionsService.findAllSubscriptions(), HttpStatus.OK);
	}
	
	/**
	 * @param email (to find the user)
	 * @param name  (to find the event type)
	 */
	
	 @PostMapping("/Subscriptions/register")
	 @ResponseBody
	 public ResponseEntity<String> register(@RequestParam("email") String email,
	                                        @RequestParam("name") String name) 
	 {
	        User user = userService.findUserByEmail(email);
	        EventType eventType = eventTypeService.findEventTypeByName(name);
	                       
	        if (user != null && eventType != null) {
	        	Subscriptions inSubscription = subscriptionsService.findSubscriptionsByType(eventType);
	        	if(inSubscription != null) {
	        		inSubscription.getUsers().add(user);
	        		subscriptionsService.saveSubcribe(inSubscription);
	        		return new ResponseEntity<>("Subscription Successful.", HttpStatus.ACCEPTED);
	        	}else {
	        		Subscriptions subscription = new Subscriptions();
	        		subscription.setEventType(eventType);
	        		// Creating an empty Set 
	                Set<User> u = new HashSet<User>();
	                u.add(user);
		        	subscription.setUsers(u);
		        	subscriptionsService.saveSubcribe(subscription);
		            return new ResponseEntity<>("Subscription Successful.", HttpStatus.CREATED);
	        	}
	        }
	        return new ResponseEntity<>("An error occurred during your subscription.", HttpStatus.NOT_FOUND);     
	    }
	 
	 /**
	  * @param email  (The list of events types to which a user has subscribed)
	  * @return 
	  */
	 @GetMapping("/Subscriptions/mySubscription")
	 @ResponseBody
	 public ResponseEntity<List<EventType>> userSubscriptions(@RequestParam("email") String email) {
		 
		 User user = userService.findUserByEmail(email);
         
	        if (user != null) {
	        	List<Subscriptions> allSubscription = subscriptionsService.findAllSubscriptions();
	        	// Creating an empty List
	        	List<EventType> ListEventType = new ArrayList<EventType>();
	        	if(allSubscription != null) {
	        		for(Subscriptions sub : allSubscription) {
	        			if(sub.getUsers() != null) {
	        				for(User u : sub.getUsers()) {
	        					if(u == user) ListEventType.add(sub.getEventType());
	        				}
	        			}
	        		}	
	        	}
	        	return new ResponseEntity<>(ListEventType, HttpStatus.OK);
	        }
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	 }
	 
	 /**
	  * @param name (delete a subscription by event type)
	  * @return
	  */
	 @PostMapping("/Subscriptions/deleteSubscription")
	 @ResponseBody
	 public ResponseEntity<String> deleteSubscription(@RequestParam("name") String name) 
	 {
		EventType eventType = eventTypeService.findEventTypeByName(name);
	        	               
	     if (eventType != null) {
	    	 Subscriptions inSubscription = subscriptionsService.findSubscriptionsByType(eventType);
	    	  boolean done =  subscriptionsService.deleteSubscription(inSubscription);
	    	  if(done == true) return new ResponseEntity<>("successfully deleted.", HttpStatus.ACCEPTED);
	    	  else return new ResponseEntity<>("unsuccessful deletion.", HttpStatus.NOT_MODIFIED);
          }
	      return new ResponseEntity<>("An error occurred during your operation.", HttpStatus.NOT_FOUND);    
	  }
	 
	 /**
	  * @param email (to find the user)
	  * @param name  (delete the user from the list of subscribers to this event type)
	  * @return
	  */
	 @PostMapping("/Subscriptions/delete")
	 @ResponseBody
	 public ResponseEntity<String> delete(@RequestParam("email") String email,
	                                      @RequestParam("name") String name) 
	 {
		 User user = userService.findUserByEmail(email);
	     EventType eventType = eventTypeService.findEventTypeByName(name);
	        	               
	     if (user != null && eventType != null) {
	    	 Subscriptions inSubscription = subscriptionsService.findSubscriptionsByType(eventType);
	         if(inSubscription != null) {
	        	 if(inSubscription.getUsers() != null) {
	        		 for(User u : inSubscription.getUsers()) {
	        			 if(u == user) {
	        				 inSubscription.getUsers().remove(user);
        					 subscriptionsService.saveSubcribe(inSubscription);
        					 return new ResponseEntity<>("Success: a subscription has been deleted.", HttpStatus.ACCEPTED);
        				 }
        			  }
	        		 return new ResponseEntity<>("No change.", HttpStatus.NOT_MODIFIED);
	        	  }
	         }
	      }
	      return new ResponseEntity<>("An error occurred during your operation.", HttpStatus.NOT_FOUND);  
	  }
		 
}