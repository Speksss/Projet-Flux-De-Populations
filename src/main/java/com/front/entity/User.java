package com.front.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Scope;

@JsonIgnoreProperties(ignoreUnknown = true)
@Scope("session")
public class User {

    private String email;
    private String password;

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

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
