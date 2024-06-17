package it.epicode.whatsnextbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class Role {

    public static final String ROLES_ADMIN = "ADMIN";
    public static final String ROLES_USER = "USER";

    @Id
    @Column(length = 30)
    private String typeRole;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<User> users;

    public Role(String typeRole) {
        this.typeRole = typeRole;
    }
}
