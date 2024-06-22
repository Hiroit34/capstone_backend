package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.model.Role;
import it.epicode.whatsnextbe.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role create(Role role){
        return roleRepository.save(role);
    }

    public boolean existsByRoleType(String roleType) {
        return roleRepository.existsById(roleType);
    }
}
