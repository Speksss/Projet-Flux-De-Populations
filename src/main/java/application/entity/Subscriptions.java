package application.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author backTeam
 * Subscription entity
 */

/**
 * Entité Abonnement
 */
@Entity
@Table(name = "subscribe")
public class Subscriptions {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	@Column(name="subscribe_id")
    private Long id;
	
	@OneToOne()
    @JoinColumn(name = "event_type_id")
	private EventType eventType;
	
	@ManyToMany()
	@JoinTable(
            name = "users_subsribes",
            joinColumns = @JoinColumn(name = "subscribe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users;
	
	public Subscriptions() {}
	
	public Subscriptions(Set<User> users, EventType eventType) {
		super();
		this.users = users;
		this.eventType = eventType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	
}
