package it.epicode.whatsnextbe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "category")
public class Category extends BaseEntity{

    @Column(unique=true, nullable=false, length=50)
    private String categoryType;

    @OneToMany (
            mappedBy = "category",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER
    )
    private List<Task> tasks;

}
