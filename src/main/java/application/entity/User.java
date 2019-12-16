package application.entity;

import application.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Entité Utilisateur
 */
@Entity
@Table(name = "User", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name="user_id")
    private long id;

    private String lastName;

    private String firstName;

    private String email;

//    private boolean isActive;

    private long creationTimestamp;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "user_location_id")
    private UserLocation userLocation;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
    }

    public User(String email, String lastName, String firstName, String password) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
//        this.isActive = false;
        this.creationTimestamp = System.currentTimeMillis()/1000;
    }

    public User(String email, String lastName, String firstName, String password, Set<Role> roles) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.roles = roles;
//        this.isActive = false;
        this.creationTimestamp = System.currentTimeMillis()/1000;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

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

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }


//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean hasRole(String role) {
        boolean flag = false;
        for (Role r : this.roles) {
            if (r.getRole().equals(role)) {
                flag = true;
                break;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
