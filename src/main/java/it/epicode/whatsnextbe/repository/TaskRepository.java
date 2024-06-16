package it.epicode.whatsnextbe.repository;

import it.epicode.whatsnextbe.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
