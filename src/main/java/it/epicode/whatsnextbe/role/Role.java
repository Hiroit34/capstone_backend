package it.epicode.whatsnextbe.role;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table (name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50)
    private String name;
}
