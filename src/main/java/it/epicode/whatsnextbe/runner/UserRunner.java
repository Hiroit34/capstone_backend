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

@Component
@Order(14)
public class UserRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role userRole = roleRepository.findById(Role.ROLES_USER).orElse(null);
        if (userRole == null) {
            userRole = new Role();
            userRole.setTypeRole(Role.ROLES_ADMIN);
            roleRepository.save(userRole);
        }

        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setFirstName("Franz");
            user.setLastName("Test");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setEmail("franzTest@esempio.com");
            user.setRoles(Collections.singletonList(userRole));

            userRepository.save(user);

            System.out.println("UTENTE INSERITO");
        }

        if (!userRepository.existsByUsername("user1")) {
            User user = new User();
            user.setFirstName("Franz");
            user.setLastName("Test");
            user.setUsername("user1");
            user.setPassword(passwordEncoder.encode("password"));
            user.setEmail("franzTest1@esempio.com");
            user.setRoles(Collections.singletonList(userRole));

            userRepository.save(user);

            System.out.println("UTENTE NUMERO 1 INSERITO");
        }
    }
}
