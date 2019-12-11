package application.service;

import application.entity.User;
import application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public void saveNewUser(User user) {
        user.setPassword(encodePassword(user));
        userRepository.save(user);
        log.info("saveNewUser() : {}", user.toString());
    }

    public String encodePassword(User user) {
        return bCryptPasswordEncoder.encode(user.getPassword());
    }
}