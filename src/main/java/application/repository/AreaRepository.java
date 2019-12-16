package application.repository;

import application.entity.Area;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AreaRepository extends CrudRepository<Area, Long> {

    Area findById(Integer id);
    Area findByName(String name);
//    List<Area> findByCoordinates(String coordinates);

    List<Area> findAll();

    void delete(Area area);

}
