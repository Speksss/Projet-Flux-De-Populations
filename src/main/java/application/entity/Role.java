package application.entity;

import application.utils.RoleType;

import javax.persistence.*;
import java.util.Set;

/**
 * Entité Role (Etudiant, professeur, admin)
 */
@Entity
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    public Role() {}

    public Role(RoleType role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
