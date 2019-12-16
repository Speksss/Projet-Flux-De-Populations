package application.service;

import application.entity.Area;
import application.entity.Event;
import application.entity.EventType;
import application.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Services liés aux evenements
 */
@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    /**
     * CRUD des Events
     *
     * @param event
     */
    public void saveNewEvent(Event event) {
        eventRepository.save(event);
        log.info("saveNewEvent() : {}", event.toString());
    }

    public Event findEventById(long id) {
        return this.eventRepository.findById(id);
    }

    public List<Event> findEventsByArea(Area area) {
        return this.eventRepository.findByArea(area);
    }

    public List<Event> findEventsByType(EventType eventType) {
        return this.eventRepository.findByEventType(eventType);
    }

    public List<Event> findEventsByDate(Date date) {
        return this.eventRepository.findByDate(date);
    }

    public List<Event> findAllEvents() {
        return this.eventRepository.findAll();
    }

    public List<Event> findAllActiveEvents() {
        return this.eventRepository.findAllByActiveIsTrue();
    }

}
