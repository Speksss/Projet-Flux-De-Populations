package application.entity;

import application.utils.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Entité Utilisateur
 */
@Entity
@Table(name = "User", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "user_id")
    private long id;

    private String lastName;

    private String firstName;

    private String email;

    private boolean active;

    private long creationTimestamp;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "user_location_id")
    private UserLocation userLocation;

    @NotEmpty
    @ElementCollection(targetClass = RoleType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Collection<RoleType> roles;

    public User() {
    }

    public User(String email, String lastName, String firstName, String password) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.roles = new HashSet<>();
        this.active = true;
        this.creationTimestamp = System.currentTimeMillis() / 1000;
    }

    public User(String email, String lastName, String firstName, String password, Collection<RoleType> roles) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.roles = roles;
        this.active = true;
        this.creationTimestamp = System.currentTimeMillis() / 1000;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Collection<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleType> roles) {
        this.roles = roles;
    }

    public void addRole(RoleType role) {
        this.roles.add(role);
    }

    public boolean hasRole(String role) {
        for(RoleType r : this.roles) {
            if(r.name().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRole(RoleType role) {
        return this.hasRole(role.name());
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
        sb.append(", isActive='").append(active).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
