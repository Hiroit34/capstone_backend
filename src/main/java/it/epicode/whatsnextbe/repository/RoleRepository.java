package it.epicode.whatsnextbe.repository;

import it.epicode.whatsnextbe.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
