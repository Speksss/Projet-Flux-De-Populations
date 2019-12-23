package application.repository;

import application.entity.Schedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule, Integer> {
    Schedule findById(int id);

    List<Schedule> findAll();

    void delete(Schedule schedule);
}
