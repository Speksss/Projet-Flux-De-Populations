package application.controller;

import application.entity.Role;
import application.entity.User;
import application.entity.UserLocation;
import application.service.RoleService;
import application.service.UserLocationService;
import application.service.UserService;
import application.utils.RoleType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Controlleur dédié a la gestion des utilisateurs
 */
@RestController
@Api(value = "fluxDePopulation", description = "Opérations relatives à la gestion basique des utilisateurs", produces = "application/json")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserLocationService userLocationService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    /**
     * Renvoie la liste de tous les utilisateurs dans la BD
     * @return ReponseEntity (avec une liste)
     */
    @ApiOperation(value = "Retourne la liste de tous les utilisateurs", response = List.class)
    @GetMapping("/user/all")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(this.userService.findAllUsers(), HttpStatus.OK);
    }

    /**
     * Vérifie si le mot de passe fourni en paramètre correspond avec le mot de passe stocké dans la BD (pour l'email en paramètre)
     * @param email : addresse email permettant d'identifier l'utilsateur
     * @param password : mot de passe à vérifier
     * @return ResponseEntity avec un objet User si le mot de passe correspond et null sinon
     */
    @ApiOperation(value = "Authentifie un utilisateur par son adresse mail", response = User.class)
    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<User> login(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
        User user = userService.findUserByEmail(email);
        if (userService.comparePassword(password, user.getPassword()) && user.isActive()) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Ajoute un nouvel utilisateur dans la BD
     * @param email : email de l'utilisateur à créer
     * @param lastName : nom de l'utilisateur à créer
     * @param firstName : prénom de l'utilisateur à créer
     * @param password : mot de passe de l'utilisateur à créer
     * @return ResponseEntity avec un String en fonction du déroulement de l'opération
     */
    @ApiOperation(value = "Créé un nouvel utilisateur", response = String.class)
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestParam("email") String email,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("firstName") String firstName,
                                           @RequestParam("password") String password) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            return new ResponseEntity<>("Cette adresse email est déjà utilisée.", HttpStatus.NOT_MODIFIED);
        } else if (!EmailValidator.getInstance().isValid(email)) {
            return new ResponseEntity<>("Cette adresse email n'est pas valide.", HttpStatus.NOT_ACCEPTABLE);
        } else {
            User newUser = new User(email, lastName, firstName, password);
            newUser.addRole(new Role(RoleType.STUDENT));
            if (this.userService.saveUser(newUser) != null) {
                return new ResponseEntity<>("L'utilisateur a correctement été créé.", HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Une erreur est survenue lors de la création de votre compte.", HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * Modifier un ou plusieurs attributs d'un utilisateur si le mot de passe fourni correspond (email non modifiable)
     * @param email : email de l'utilsateur (pour l'identifier)
     * @param password : mot de passe de l'utilisateur
     * @param lastName : nom à modifier (OPTIONNEL)
     * @param firstName : prénom à modifier (OPTIONNEL)
     * @param newPassword : mot de passe à modifier (OPTIONNEL)
     * @return ResponseEntity avec un String en fonction du déroulement de l'opération
     */
    @ApiOperation(value = "Modifie un utilisateur grâce à son adresse email", response = String.class)
    @PostMapping("/user/update")
    @ResponseBody
    public ResponseEntity<String> update(@RequestParam("email") String email,
                                         @RequestParam(value = "password") String password,
                                         @RequestParam(value = "lastName", required = false) String lastName,
                                         @RequestParam(value = "firstName", required = false) String firstName,
                                         @RequestParam(value = "newPassword", required = false) String newPassword) {
        User user = this.userService.findUserByEmail(email);
        if (userService.comparePassword(password, user.getPassword())) {
            if (lastName != null) {
                user.setLastName(lastName);
            }
            if (firstName != null) {
                user.setFirstName(firstName);
            }
            if (newPassword != null) {
                user.setPassword(userService.encodePassword(newPassword));
            }
            if (this.userService.updateUser(user) != null) {
                return new ResponseEntity<>("L'utilisateur a correctement été modifié.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Une erreur est survenue lors de la modification de votre compte.", HttpStatus.NOT_MODIFIED);
        } else {
            return new ResponseEntity<>("Le mot de passe saisi est incorrect.", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Supprime un utilisateur de la BD (si le mot de passe correspond)
     * @param email : email de l'utilisateur à supprimer
     * @param password : mot de passe de l'utilisateur
     * @return ResponseEntity avec un String en fonction du déroulement de l'opération
     */
    @ApiOperation(value = "Supprime un utilisateur par son adresse mail", response = String.class)
    @DeleteMapping("/user/delete")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestParam("email") String email,
                                         @RequestParam(value = "password") String password) {
        User user = userService.findUserByEmail(email);
        if (userService.comparePassword(password, user.getPassword())) {
            if (this.userService.deleteUser(user)) {
                return new ResponseEntity<>("L'utilisateur a correctement été supprimé.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Une erreur est survenue lors de la suppression de votre compte.", HttpStatus.NOT_MODIFIED);
            }
        } else {
            return new ResponseEntity<>("Le mot de passe saisi est incorrect.", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Modifie les coordonnées de localisation d'un utilisateur
     * @param userEmail : email de l'utilisateur à modifier
     * @param latitutde : latitude de la nouvelle position
     * @param longitude : longitude de la nouvelle position
     * @return ResponseEntity avec un String en fonction du déroulement de l'opération
     */
    @ApiOperation(value = "Modification de la localisation de l'utilisateur", response = String.class)
    @PostMapping("/location/update")
    @ResponseBody
    public ResponseEntity<String> updateLocation(
            @RequestParam("userMail")String userEmail,
            @RequestParam("latitude")double latitutde,
            @RequestParam("longitude")double longitude
    ) {
        User user = userService.findUserByEmail(userEmail);
        if(user != null) {
            UserLocation userL = user.getUserLocation();
            userL.setLatitude(latitutde);
            userL.setLongitude(longitude);
            userLocationService.saveUserLocation(userL);

            return new ResponseEntity<>("La position de l'utilisateur à été actualisée.",HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>("L'utilisateur n'existe pas",HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Retoure l'ensemble des utlisateurs d'une zone
     * @return List de user
     */
    @ApiOperation(value = "Retourne la liste des utlisateurs dans une zone", response = List.class)
    @GetMapping("/user/area/{id}")
    @ResponseBody
    public List<User> getAllUserByArea( @PathVariable(value="id")Integer id){
        System.out.println("[GET] All UserByArea");
        return userService.findAllUsersByArea(id);
    }

    /**
     * Supprime un utilisateur de la BD par un administrateur
     * @param emailAdmin : email de l'administrateur
     * @param emailUser : email de l'utilisateur à supprimer
     * @return ResponseEntity avec un String en fonction du déroulement de l'opération
     */
    @ApiOperation(value = "Supprime un utilisateur par son adresse mail (Admin)", response = String.class)
    @DeleteMapping("/admin/user/delete")
    @ResponseBody
    public ResponseEntity<String> deleteAdmin(@RequestParam("emailAdmin") String emailAdmin,
                                              @RequestParam("emailUser") String emailUser) {
        User userAdmin = userService.findUserByEmail(emailAdmin);
        if (userAdmin.hasRole(RoleType.ADMIN)) {
            User user = userService.findUserByEmail(emailUser);
            if (user != null) {
                if (this.userService.deleteUser(user)) {
                    return new ResponseEntity<>("L'utilisateur a correctement été supprimé.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Une erreur est survenue lors de la suppression de votre compte.", HttpStatus.NOT_MODIFIED);
                }
            } else {
                return new ResponseEntity<>("Cet utilisateur n'existe pas.", HttpStatus.NOT_MODIFIED);
            }
        } else {
            return new ResponseEntity<>("Vous n'avez pas l'autorisation pour effectuer cette opération.", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Modifier un ou plusieurs attributs d'un utilisateur (par un Admin)
     * @param email : email de l'utilsateur (pour l'identifier)
     * @param password : mot de passe de l'utilisateur à modifier (OPTIONNEL)
     * @param lastName : nom à modifier (OPTIONNEL)
     * @param firstName : prénom à modifier (OPTIONNEL)
     * @return ResponseEntity avec un String en fonction du déroulement de l'opération
     */
    @ApiOperation(value = "Modifie un utilisateur grâce à son adresse email (par un admin)", response = String.class)
    @PostMapping("/admin/user/update")
    @ResponseBody
    public ResponseEntity<String> updateAdmin(@RequestParam("emailAdmin") String emailAdmin,
                                              @RequestParam("email") String email,
                                              @RequestParam(value = "password", required = false) String password,
                                              @RequestParam(value = "lastName", required = false) String lastName,
                                              @RequestParam(value = "firstName", required = false) String firstName,
                                              @RequestParam(value = "isActive", required = false) Boolean isActive) {
        User userAdmin = userService.findUserByEmail(emailAdmin);
        if (userAdmin.hasRole(RoleType.ADMIN)) {
            User user = this.userService.findUserByEmail(email);
            if (password != null) {
                user.setPassword(userService.encodePassword(password));
            }
            if (lastName != null) {
                user.setLastName(lastName);
            }
            if (firstName != null) {
                user.setFirstName(firstName);
            }
            if (isActive != null) {
                user.setActive(isActive);
            }
            if (this.userService.updateUser(user) != null) {
                return new ResponseEntity<>("L'utilisateur a correctement été modifié.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Une erreur est survenue lors de la modification de votre compte.", HttpStatus.NOT_MODIFIED);
        } else {
            return new ResponseEntity<>("Vous n'avez pas l'autorisation pour effectuer cette opération.", HttpStatus.FORBIDDEN);
        }
    }


}
