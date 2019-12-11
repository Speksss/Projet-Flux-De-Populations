package application.controller;

import application.entity.User;
import application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    @ResponseBody
    public User login(@RequestParam(value="email") String email, @RequestParam(value="password") String password) {
        User user = userService.findUserByEmail(email);
        log.info("[*] LOGIN : " + user.getPassword() + "(password)");
        if (userService.comparePassword(password, user.getPassword())) {
            log.info("[*] LOGIN : " + true);
            return user;
        }
        return null;
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestParam("email") String email,
                            @RequestParam("nom") String nom,
                            @RequestParam("prenom") String prenom,
                            @RequestParam("password") String password) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            return "Cette adresse email est déjà utilisée.";
        } else {
            User newUser = new User(email, nom, prenom, password);
            if (this.userService.saveNewUser(newUser) != null) {
                return "L'utilisateur a correctement été créé.";
            }
            return "Une erreur est survenue lors de la création de votre compte";
        }
    }
}
