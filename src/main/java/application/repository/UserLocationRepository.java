package application.repository;

import application.entity.UserLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserLocationRepository extends CrudRepository<UserLocation,Integer> {
    UserLocation findById(long id);
    List<UserLocation> findAll();

    @Query("SELECT u FROM UserLocation u WHERE u.inZone = true")
    List<UserLocation> findAllInZone();
}
