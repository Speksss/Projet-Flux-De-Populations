package application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Date;

/**
 * Entité Evenement
 */
@Entity
@Table(name = "event")
public class Event {

    @Id
//    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="event_id")
    private Integer id;

    private String name;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne()
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @ManyToOne()
    @JoinColumn(name = "area_id")
    private Area area;

    private boolean active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return eventType == null ? "" : eventType.getDescription();
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area userLocation) {
        this.area = userLocation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return active == event.active &&
                Objects.equal(id, event.id) &&
                Objects.equal(name, event.name) &&
                Objects.equal(date, event.date) &&
                Objects.equal(eventType, event.eventType) &&
                Objects.equal(area, event.area);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", eventType=" + eventType +
                ", area=" + area +
                ", active=" + active +
                '}';
    }
}
