package it.epicode.whatsnextbe.repository;

import it.epicode.whatsnextbe.model.Task;
import it.epicode.whatsnextbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE :userId MEMBER OF t.users OR t.isShared = true")
    List<Task> findAllByUsersIdOrShared(@Param("userId") Optional<User> userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM task WHERE id = ?1", nativeQuery = true)
    void deleteById(Long id);

    List<Task> findByCategoryId(Long categoryId);
}
