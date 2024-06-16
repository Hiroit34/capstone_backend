package it.epicode.whatsnextbe.repository;

import it.epicode.whatsnextbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
