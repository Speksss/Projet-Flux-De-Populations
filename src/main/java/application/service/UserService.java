package application.service;

import application.entity.User;
import application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public User saveUser(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        log.info("saveNewUser() : {}", user.toString());
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(User user) {
        this.userRepository.delete(user);
        return this.userRepository.findByEmail(user.getEmail()) == null;
    }

    public User findUserById(long id) {
        return this.userRepository.findById(id);
    }

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public List<User> findUsersByLastName(String lastName) {
        return this.userRepository.findByLastName(lastName);
    }

    public List<User> findUsersByFirstName(String firstName) {
        return this.userRepository.findByFirstName(firstName);
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public boolean comparePassword(String rawPassword, String encryptedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encryptedPassword);
    }
}