package it.epicode.whatsnextbe.runner;

import it.epicode.whatsnextbe.model.Role;
import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.repository.RoleRepository;
import it.epicode.whatsnextbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@Order(15)
public class AdminRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Role adminRole = roleRepository.findById(Role.ROLES_ADMIN).orElse(null);
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setTypeRole(Role.ROLES_ADMIN);
            roleRepository.save(adminRole);
        }

        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@example.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Collections.singletonList(adminRole));

            userRepository.save(admin);
        }
    }
}
