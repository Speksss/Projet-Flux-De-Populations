package application.repository;

import application.entity.Capteur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CapteurRepository extends CrudRepository<Capteur,Long> {
    Capteur findById(Integer id);
    List<Capteur> findAll();
}
