package application.repository;

import application.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findById(long id);
    User findByEmail(String email);
    List<User> findByLastName(String lastName);
    List<User> findByFirstName(String firstName);
    List<User> findAll();

}
