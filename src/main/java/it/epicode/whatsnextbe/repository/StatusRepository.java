package it.epicode.whatsnextbe.repository;

import it.epicode.whatsnextbe.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
