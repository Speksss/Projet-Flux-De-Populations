package application.repository;

import application.entity.Area;
import application.entity.Event;
import application.entity.EventType;
import application.entity.UserLocation;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository  extends CrudRepository<Event, Long> {

    Event findById(long id);

    List<Event> findByEventType(EventType eventType);

    List<Event> findByDate(Date date);

    List<Event> findByArea(Area area);

    List<Event> findAll();

    List<Event> findAllByActiveIsTrue();
}
