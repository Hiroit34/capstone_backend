package it.epicode.whatsnextbe.task;

import it.epicode.whatsnextbe.category.Category;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50)
    private String tile;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "category_id")
    private Category categories;
}
