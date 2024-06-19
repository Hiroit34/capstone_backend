package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationSerivce {

    @Autowired
    private UserRepository userRepository;

    public boolean isAdmin(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(User::isAdmin).orElse(false);
    }

}
