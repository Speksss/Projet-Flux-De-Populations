package application.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import application.entity.EventType;
import application.entity.Subscriptions;

/**
 * 
 * @author backTeam
 * Subscription Repository
 */
public interface SubscriptionsRepository extends CrudRepository<Subscriptions, Long>{
	
	Subscriptions findById(long id);
	Subscriptions findByEventType(EventType eventType);
    List<Subscriptions> findAll();
    
    void delete(Subscriptions subscription);

}
