package application.service;

import application.entity.Area;
import application.entity.User;
import application.entity.UserLocation;
import application.repository.UserRepository;
import application.utils.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Services liés aux utilisateurs
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AreaService areaService;

    @Autowired
    UserLocationService userLocationService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Sauvegarde d'un utilisateur dans la BD
     * @param user : l'utilisateur à sauvegarder
     * @return un objet User correspondant aux informations stockées
     */
    public User saveUser(User user) {
        UserLocation userLocation = new UserLocation();
        userLocationService.saveUserLocation(userLocation);
        user.setUserLocation(userLocation);
        user.setPassword(encodePassword(user.getPassword()));
        log.info("saveNewUser() : {}", user.toString());
        return userRepository.save(user);
    }

    /**
     * Mofifie un utilisateur
     * @param user : l'utilisateur à modifier
     * @return un objet User correspondant aux informations stockées
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Supprimer un utilisateur de la BD
     * @param user : l'utilisateur à supprimer
     * @return TRUE / FALSE en fonction de la réussite de l'opération
     */
    public boolean deleteUser(User user) {
        this.userRepository.delete(user);
        return this.userRepository.findByEmail(user.getEmail()) == null;
    }

    /**
     * Recherche d'un utilisateur par son ID
     * @param id : id de l'utilisateur à chercher
     * @return User ou NUll (si pas d'utilisateur avec l'id en paramètre)
     */
    public User findUserById(long id) {
        return this.userRepository.findById(id);
    }

    /**
     * Recherche d'un utilisateur par son addresse email
     * @param email : email de l'utilisateur à chercher
     * @return User ou NUll (si pas d'utilisateur avec l'email en paramètre)
     */
    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    /**
     * Recherche des utilisateurs par leur nom
     * @param lastName : nom de(s) utilisateur(s) à chercher
     * @return List d'utilisateurs ou NULL
     */
    public List<User> findUsersByLastName(String lastName) {
        return this.userRepository.findByLastName(lastName);
    }

    /**
     * Recherche des utilisateurs par leur prénom
     * @param firstName : prénom de(s) utilisateur(s) à chercher
     * @return List d'utilisateurs ou NULL
     */
    public List<User> findUsersByFirstName(String firstName) {
        return this.userRepository.findByFirstName(firstName);
    }

    /**
     * Recherche tous les utilisateurs de la BD
     * @return List d'utilisateurs
     */
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    public List <User> findAllUsersByArea(Integer id){
        List<UserLocation> userLocations = userLocationService.getAll();
        Area a = areaService.findAreaById(id);
        if(a == null )
            return null;
        List<UserLocation> goodUserLocation=new ArrayList<>();
        for(UserLocation uL : userLocations) {
            Point p = new Point(
                    uL.getLatitude(),
                    uL.getLongitude()
            );
            if (this.areaService.isPointInArea(a, p )){
                goodUserLocation.add(uL);
            }

        }
        List<User> userlistes= new ArrayList<>();

        for(UserLocation gd: goodUserLocation){
            User u = userRepository.findByUserLocation(gd);
            if(u !=null){
                userlistes.add(u);
            }
        }
      return  userlistes;
    }


    /**
     * Crypte un mot de passe avec le BCryptPasswordEncoder
     * @param password : string à encoder
     * @return String encodée
     */

    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * Compare un mot de passe décrypté et sa version cryptée
     * @param rawPassword : le mot de passe classique
     * @param encryptedPassword : le mot de passe crypté
     * @return true ou false
     */
    public boolean comparePassword(String rawPassword, String encryptedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encryptedPassword);
    }
}