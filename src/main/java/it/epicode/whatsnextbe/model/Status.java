package it.epicode.whatsnextbe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "status")
public class Status extends BaseEntity{

    private String status;

    @OneToMany(
            cascade = CascadeType.PERSIST,
            mappedBy = "status",
            fetch = FetchType.EAGER
    )
    private List<Task> tasks;

}
