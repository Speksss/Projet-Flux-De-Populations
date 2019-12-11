package application.service;

import application.entity.Event;
import application.entity.EventType;
import application.entity.Location;
import application.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    EventRepository eventRepository;

    /**
     * CRUD des Events
     * @param event
     */
    public void saveNewEvent(Event event) {
        eventRepository.save(event);
        log.info("saveNewEvent() : {}", event.toString());
    }

    public Event findEventById(long id) {
        return this.eventRepository.findById(id);
    }

    public List<Event> findEventsByLocation(Location location) {
        return this.eventRepository.findByLocation(location);
    }

    public List<Event> findEventsByType(EventType eventType) {
        return this.eventRepository.findByEventTypeId(eventType);
    }

    public List<Event> findEventsByDate(Date date) {
        return this.eventRepository.findByDate(date);
    }

    public List<Event> findAllEvents() {
        return this.eventRepository.findAll();
    }

}
