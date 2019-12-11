package application.controller;

import application.entity.User;
import application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    @ResponseBody
    public User login(@RequestParam(value="email") String email, @RequestParam(value="password") String password) {
        //User user = userService.findByEmail(email);
        return null;
    }
}
