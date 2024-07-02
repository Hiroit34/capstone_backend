package it.epicode.whatsnextbe.repository;

import it.epicode.whatsnextbe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryType(String categoryType);

}
