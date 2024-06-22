package it.epicode.whatsnextbe.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "task")
@ToString(exclude = {"status", "category", "users"} )
public class Task extends BaseEntity{

    @Column(unique=true, length = 50)
    private String title;
    @Column(length = 50)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(
            mappedBy = "tasks",
            cascade = {CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    private boolean isShared;

    private boolean isDeleted = false;

}
