package application.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.EventType;
import application.entity.Subscriptions;
import application.repository.SubscriptionsRepository;

/**
 * Services liés aux zones
 */
@Service
public class SubscriptionsService {
	
	 private static final Logger log = LoggerFactory.getLogger(EventService.class);

	 @Autowired
	 SubscriptionsRepository subscribptionsRepository;
	
	/**
     * CRUD des Abonnements
     * @param subscribe
     */
    public void saveSubcribe(Subscriptions subscribe) {
    	subscribptionsRepository.save(subscribe);
        log.info("saveNewSubcribe() : {}", subscribe.toString());
    }
    
    /**
     * @param id
     * @return L'abonnement
     */
    public Subscriptions findUserById(long id) {
        return this.subscribptionsRepository.findById(id);
    }

    /**
     * @param eventType
     * @return L'abonnement
     */
    public Subscriptions findSubscriptionsByType(EventType eventType) {
        return this.subscribptionsRepository.findByEventType(eventType);
    }
    
    /**
     * @return Tous les abonnements
     */
    public List<Subscriptions> findAllSubscriptions() {
        return this.subscribptionsRepository.findAll();
    }
    
    /**
     * @param subscription
     * @return boolean 
     */
    public boolean deleteSubscription(Subscriptions subscription) {
        this.subscribptionsRepository.delete(subscription);
        return this.subscribptionsRepository.findByEventType(subscription.getEventType()) == null;
    }
}
