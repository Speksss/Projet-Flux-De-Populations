package application.controller;

import application.entity.User;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/user/all")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(this.userService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<User> login(@RequestParam(value="email") String email, @RequestParam(value="password") String password) {
        User user = userService.findUserByEmail(email);
        if (userService.comparePassword(password, user.getPassword())) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestParam("email") String email,
                            @RequestParam("lastName") String lastName,
                            @RequestParam("firstName") String firstName,
                            @RequestParam("password") String password) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            return new ResponseEntity<>("Cette adresse email est déjà utilisée.", HttpStatus.NOT_MODIFIED);
        } else {
            User newUser = new User(email, lastName, firstName, password);
            if (this.userService.saveUser(newUser) != null) {
                return new ResponseEntity<>("L'utilisateur a correctement été créé.", HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Une erreur est survenue lors de la création de votre compte.", HttpStatus.NOT_MODIFIED);
        }
    }

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
}
