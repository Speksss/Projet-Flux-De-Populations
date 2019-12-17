package application.service;

import application.entity.UserLocation;
import application.repository.UserLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Services liés aux positions des utilisateurs
 */
@Service
public class UserLocationService {
    private static final Logger log = LoggerFactory.getLogger(UserLocationService.class);

    @Autowired
    UserLocationRepository userLocationRepository;

    /**
     * Sauvegarde la position d'un utilisateur
     * @param ul : La localisation d'un utilisateur à sauvegarder
     */
    public void saveUserLocation(UserLocation ul){
        this.userLocationRepository.save(ul);
    }

    /**
     * Recupere toutes les localisations des utilisateurs
     * @return La liste de toutes les positions des utilisateurs
     */
    public List<UserLocation> getAll(){
        return this.userLocationRepository.findAll();
    }

    public List<UserLocation> getAllNotNull(){return this.userLocationRepository.findAllNotNull();}

}
