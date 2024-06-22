package it.epicode.whatsnextbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "status")
@ToString(exclude = "tasks")
public class Status extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private StatusType status;

    @OneToMany(
            cascade = CascadeType.PERSIST,
            mappedBy = "status",
            fetch = FetchType.EAGER
    )
    @JsonIgnore
    private List<Task> tasks;

    public Status(StatusType status) {
        this.status = status;
    }

    public enum StatusType {
        COMPLETATO,
        NON_COMPLETATO,
        IN_CORSO
    }
}
