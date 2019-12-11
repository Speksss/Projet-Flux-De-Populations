package application.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "event_type_id")
    private EventType eventTypeId;

    private String name;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    private Location location;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EventType getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(EventType eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
