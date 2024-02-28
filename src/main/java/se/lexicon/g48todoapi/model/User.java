package se.lexicon.g48todoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class User {

    @Id
    @Column(updatable = false)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;
    private boolean expired;

    @ManyToMany
    @JoinTable(
            name = "users-roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void addRole(Role role) {
        if (role == null) throw new IllegalArgumentException("Role is null");
        if (roles == null) roles = new HashSet<>();
        roles.add(role);
    }

    public void removeRole(Role role) {
        if (role == null) throw new IllegalArgumentException("Role is null");
        if (roles != null) {
            roles.remove(role);

        } else {
            // todo: throw exception if neede
        }
    }
}
