package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.repository.UserRepository;
import it.epicode.whatsnextbe.security.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationSerivce {

    private final UserRepository userRepository;

    public boolean isAdmin(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(User::isAdmin).orElse(false);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        System.out.println(authentication.getPrincipal());
        return userDetails.getId();
    }

}
