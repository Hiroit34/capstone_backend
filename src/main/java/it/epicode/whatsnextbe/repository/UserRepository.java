package it.epicode.whatsnextbe.repository;

import it.epicode.whatsnextbe.dto.response.user.UserResponseLight;
import it.epicode.whatsnextbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findOneByUsername(String username);
    Optional<User> findByUsername(String username);
}
