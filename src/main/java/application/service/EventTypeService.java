package application.service;

import application.entity.Event;
import application.entity.EventType;
import application.repository.EventTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTypeService {

    private static final Logger log = LoggerFactory.getLogger(EventTypeService.class);

    @Autowired
    private EventTypeRepository eventTypeRepository;

    /**
     * CRUD Event
     *
     * @param event
     */
    public EventType saveNewEventType(EventType event) {
        log.info("save New EventType() : {}", event.toString());
        return eventTypeRepository.save(event);
    }

    public EventType findEventTypeById(int id) {
        return this.eventTypeRepository.findById(id);
    }

    public EventType findEventTypeByName(String name) {
        return this.eventTypeRepository.findByName(name);
    }

    public List<EventType> findAllEventTypes() {
        return this.eventTypeRepository.findAll();
    }

}