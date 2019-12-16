package application.repository;

import application.entity.User;
import application.entity.Area;
import application.entity.UserLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findById(long id);
    User findByUserLocation(UserLocation ur);
    User findByEmail(String email);
    List<User> findByLastName(String lastName);
    List<User> findByFirstName(String firstName);
    List<User> findAll();

    void delete(User user);

   // List<User> findByArea(Area area);

}
