package application.repository;

import application.entity.UserLocation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserLocationRepository extends CrudRepository<UserLocation,Integer> {
    UserLocation findById(long id);
    List<UserLocation> findAll();
}
