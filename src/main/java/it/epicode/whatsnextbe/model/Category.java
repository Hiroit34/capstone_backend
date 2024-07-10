package it.epicode.whatsnextbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "category")
@ToString(exclude = "tasks")
public class Category extends BaseEntity{

    @Column(unique=true, nullable=false, length=50)
    private String categoryType;

    @Column(nullable=false, length=500)
    private String description;

    @OneToMany (
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Task> tasks;

}
