package application.service;

import application.entity.UserLocation;
import application.repository.UserLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLocationService {
    private static final Logger log = LoggerFactory.getLogger(UserLocationService.class);

    @Autowired
    UserLocationRepository userLocationRepository;

    public void saveUserLocation(UserLocation ul){
        this.userLocationRepository.save(ul);
    }

    public List<UserLocation> getAll(){
        return this.userLocationRepository.findAll();
    }

}
