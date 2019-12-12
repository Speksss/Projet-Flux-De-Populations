package application.repository;

import application.entity.Event;
import application.entity.EventType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventTypeRepository extends CrudRepository<EventType, Integer> {

    List<EventType> findAll();
    EventType findById(int id);
    EventType findByName(String name);
}
