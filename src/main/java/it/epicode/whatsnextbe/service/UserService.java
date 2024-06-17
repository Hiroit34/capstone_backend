package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.register.RegisterRequest;
import it.epicode.whatsnextbe.dto.request.user.UserRequest;
import it.epicode.whatsnextbe.dto.response.login.LoginResponse;
import it.epicode.whatsnextbe.dto.response.register.RegisterResponse;
import it.epicode.whatsnextbe.dto.response.user.UserResponse;
import it.epicode.whatsnextbe.model.Role;
import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.repository.RoleRepository;
import it.epicode.whatsnextbe.repository.UserRepository;
import it.epicode.whatsnextbe.security.InvalidLoginException;
import it.epicode.whatsnextbe.security.JwtUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    // GET, GET BY ID, POST, PUT, DELETE

    private final PasswordEncoder encoder;
    private final AuthenticationManager auth;
    private final JwtUtils jwt;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // GET ALL
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET BY ID
    public UserResponse getUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        User entity = userRepository.findById(id).get();
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    // POST - USER
    public RegisterResponse registerUser(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new EntityExistsException("User with username " + request.getUsername() + " already exists");
        }
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("User with email " + request.getEmail() + " already exists");
        }
        Role role = roleRepository.findById(Role.ROLES_USER).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setPassword(encoder.encode(request.getPassword()));
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        RegisterResponse response = new RegisterResponse();
        BeanUtils.copyProperties(savedUser, response);
        response.setRoles(List.of(role));
        return response;
    }

    // DELETE
    public String deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
        return "User with id " + id + " deleted";
    }

    //POST - ADMIN
    public RegisterResponse registerAdmin(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EntityExistsException("Email gia' registrata");
        }
        Role role = roleRepository.findById(Role.ROLES_ADMIN).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        User u = new User();
        BeanUtils.copyProperties(request, u);
        u.setPassword(encoder.encode(request.getPassword()));
        if (u.getRoles() == null) {
            u.setRoles(new ArrayList<>());
        }
        u.getRoles().add(role);
        User savedUser = userRepository.save(u);
        RegisterResponse response = new RegisterResponse();
        BeanUtils.copyProperties(savedUser, response);
        response.setRoles(List.of(role));
        return response;
    }

    public Optional<LoginResponse> login(String username, String password) {
        try {
            //SI EFFETTUA IL LOGIN
            //SI CREA UNA AUTENTICAZIONE OVVERO L'OGGETTO DI TIPO AUTHENTICATION
            var a = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            a.getAuthorities(); //SERVE A RECUPERARE I RUOLI/IL RUOLO

            //SI CREA UN CONTESTO DI SICUREZZA CHE SARA UTILIZZATO IN PIU OCCASIONI
            SecurityContextHolder.getContext().setAuthentication(a);

            var user = userRepository.findOneByUsername(username).orElseThrow();
            var dto = LoginResponse.builder()
                    .withUser(RegisterResponse.builder()
                            .withId(user.getId())
                            .withFirstName(user.getFirstName())
                            .withLastName(user.getLastName())
                            .withEmail(user.getEmail())
                            .withRoles(user.getRoles())
                            .withUsername(user.getUsername())
                            .build())
                    .build();

            //UTILIZZO DI JWTUTILS PER GENERARE IL TOKEN UTILIZZANDO UNA AUTHENTICATION E LO ASSEGNA ALLA LOGINRESPONSEDTO
            dto.setToken(jwt.generateToken(a));

            return Optional.of(dto);
        } catch (NoSuchElementException e) {
            //ECCEZIONE LANCIATA SE LO USERNAME E SBAGLIATO E QUINDI L'UTENTE NON VIENE TROVATO
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            //ECCEZIONE LANCIATA SE LA PASSWORD E SBAGLIATA
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }
    }
}
