package it.epicode.whatsnextbe.repository;

import it.epicode.whatsnextbe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
