package application.service;

import application.entity.EventType;
import application.entity.Subscription;
import application.entity.User;
import application.repository.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Services liés aux zones
 */
@Service
public class SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    /**
     * CRUD des Abonnements
     *
     * @param subscribe
     */
    public void saveSubscription(Subscription subscribe) {
        subscriptionRepository.save(subscribe);
        log.info("saveNewSubcribe() : {}", subscribe.toString());
    }

    /**
     * @param subscription
     *
     * @return boolean
     */
    public boolean deleteSubscription(Subscription subscription) {
        this.subscriptionRepository.delete(subscription);
        return this.subscriptionRepository.findByEventType(subscription.getEventType()) == null;
    }

    /**
     * @param id
     *
     * @return L'abonnement
     */
    public Subscription findSubscriptionById(long id) {
        return this.subscriptionRepository.findById(id);
    }

    /**
     * @param eventType
     *
     * @return L'abonnement
     */
    public Subscription findSubscriptionByType(EventType eventType) {
        return this.subscriptionRepository.findByEventType(eventType);
    }

    /**
     * @return Tous les abonnements
     */
    public List<Subscription> findAllSubscriptions() {
        return this.subscriptionRepository.findAll();
    }

    /**
     * @param user : l'utilisateur dont on cherche les abonnements
     *
     * @return Tous les abonnements d'un utilisateur donné
     */
    public List<Subscription> findAllSubscriptionsByUser(User user) {
        List<Subscription> subscriptions = this.subscriptionRepository.findAll();
        if(user != null) {
            subscriptions.removeIf(subscription -> !subscription.getUsers().contains(user));
        }

        return subscriptions;
    }
}
