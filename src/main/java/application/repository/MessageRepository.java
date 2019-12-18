package application.repository;

import application.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    Message findById(int id);
    List<Message> findAll();
    void delete(Message message);
}
