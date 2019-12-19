package com.front.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Scope;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Scope("session")
public class User {

    private int id = 1;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean active;
    private List<String> roles;

    public User(String email, String password, String firstname, String lastname, boolean active, List<String> roles){
        this.email = email;
        this.password = password;
        this.firstName = firstname;
        this.lastName = lastname;
        this.active = active;
        this.id = id++;
        this.roles = roles;
    }

    public User(String email, String password, String firstname, String lastname, boolean active){
        this.email = email;
        this.password = password;
        this.firstName = firstname;
        this.lastName = lastname;
        this.active = active;
        this.id = id++;
    }

    public User(String username, String password) {
        this.email = username;
        this.password = password;
    }

    public User() { }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", lastname='" + lastName + '\'' +
                ", firstname='" + firstName + '\'' +
                ", active='" + active + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
