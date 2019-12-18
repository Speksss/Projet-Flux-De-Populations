package com.front.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private int id;
    private String name;
    private Date date;
    private String description;
    private boolean active;
    private EventType eventType;
    private Area area;

    public Event(String name, Date date, String description, boolean active, EventType eventType, Area area) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.active = active;
        this.eventType = eventType;
        this.area = area;
    }

    public Event() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", eventType=" + eventType +
                ", area=" + area +
                '}';
    }
}
