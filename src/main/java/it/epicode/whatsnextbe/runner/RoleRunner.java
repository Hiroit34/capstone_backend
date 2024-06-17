package it.epicode.whatsnextbe.runner;

import it.epicode.whatsnextbe.model.Role;
import it.epicode.whatsnextbe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleRunner implements ApplicationRunner {

    @Autowired
    private RoleService service;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Verifica se i ruoli esistono già nel database
        if (!service.existsByRoleType(Role.ROLES_ADMIN)) {
            service.create(new Role(Role.ROLES_ADMIN));
            System.out.println("Ruolo ADMIN inserito");
        } else {
            System.out.println("Ruolo ADMIN già presente");
        }

        if (!service.existsByRoleType(Role.ROLES_USER)) {
            service.create(new Role(Role.ROLES_USER));
            System.out.println("Ruolo USER inserito");
        } else {
            System.out.println("Ruolo USER già presente");
        }

    }
}
