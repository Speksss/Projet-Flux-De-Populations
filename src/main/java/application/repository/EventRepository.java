package application.repository;

import application.entity.Event;
import application.entity.EventType;
import application.entity.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository  extends CrudRepository<Event, Long> {

    Event findById(long id);
    List<Event> findByEventTypeId(EventType eventTypeId);
    List<Event> findByDate(Date date);
    List<Event> findByLocation(Location locationId);

    List<Event> findAll();
}
