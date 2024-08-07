package it.epicode.whatsnextbe.repository;

import it.epicode.whatsnextbe.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByStatus(Status.StatusType status);
    boolean existsByStatus(Status.StatusType status);
}
