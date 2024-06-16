package it.epicode.whatsnextbe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "task")
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
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<User> users;


}
