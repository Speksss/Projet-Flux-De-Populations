package application.service;

import application.entity.User;
import application.repository.UserRepository;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * CRUD des Users
     * @param user
     */
    public void saveNewUser(User user) {
        user.setPassword(encodePassword(user));
        userRepository.save(user);
        log.info("saveNewUser() : {}", user.toString());
    }

    /**
     * Retourne les informations principales de l'utilisateur
     * @param u Utilisateur
     * @return Informations au format JSON
     */
    public String getUserInfo(User u){
        JSONObject json = new JSONObject();
        json.put("nom",u.getNom());
        json.put("prenom",u.getPrenom());
        json.put("email",u.getEmail());
        return json.toJSONString();
    }

    public String encodePassword(User user) {
        return bCryptPasswordEncoder.encode(user.getPassword());
    }
}