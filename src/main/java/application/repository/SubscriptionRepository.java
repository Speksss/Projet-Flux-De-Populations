package application.repository;

import application.entity.EventType;
import application.entity.Subscription;
import application.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * @author backTeam Subscription Repository
 */
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    Subscription findById(long id);

    Subscription findByEventType(EventType eventType);

    List<Subscription> findAll();

    void delete(Subscription subscription);

}
