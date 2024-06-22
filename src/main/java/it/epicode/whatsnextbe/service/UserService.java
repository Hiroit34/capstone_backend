package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.register.RegisterRequest;
import it.epicode.whatsnextbe.dto.response.login.LoginResponse;
import it.epicode.whatsnextbe.dto.response.register.RegisterResponse;
import it.epicode.whatsnextbe.dto.response.task.TaskResponseTitleAndID;
import it.epicode.whatsnextbe.dto.response.user.UserResponse;
import it.epicode.whatsnextbe.dto.response.user.UserResponseWithTaskDTO;
import it.epicode.whatsnextbe.email.EmailService;
import it.epicode.whatsnextbe.mapper.UserMapper;
import it.epicode.whatsnextbe.model.Role;
import it.epicode.whatsnextbe.model.Task;
import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.repository.RoleRepository;
import it.epicode.whatsnextbe.repository.UserRepository;
import it.epicode.whatsnextbe.security.InvalidLoginException;
import it.epicode.whatsnextbe.security.JwtUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder encoder;
    private final AuthenticationManager auth;
    private final JwtUtils jwt;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Transactional
    public List<UserResponseWithTaskDTO> getAllUsers() {
        List<User> users = userRepository.findAll();


        return users.stream().map(userMapper::convertToUserResponseDTO).collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        User entity = userRepository.findById(id).get();
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public RegisterResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new EntityExistsException("User with username " + request.getUsername() + " already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
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
        emailService.sendWelcomeEmail(user.getEmail());
        return response;
    }

    public String deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
        return "User with id " + id + " deleted";
    }

    public RegisterResponse registerAdmin(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new EntityExistsException("Utente gia' esistente");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
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
            // Recupera l'utente dal repository
            var user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new InvalidLoginException(username, password, InvalidLoginException.ErrorType.USERNAME, "Invalid username"));

            // Autentica l'utente con le credenziali fornite
            var authentication = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Recupera i ruoli dell'utente
            authentication.getAuthorities();

            // Imposta il contesto di sicurezza
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Crea la risposta di login
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

            // Genera e assegna il token JWT alla risposta di login
            dto.setToken(jwt.generateToken(authentication));

            return Optional.of(dto);
        } catch (NoSuchElementException e) {
            // Eccezione lanciata se il nome utente è errato
            log.error("User not found", e);
            throw new InvalidLoginException(username, password, InvalidLoginException.ErrorType.USERNAME, "Invalid username");
        } catch (AuthenticationException e) {
            // Eccezione lanciata se la password è errata
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password, InvalidLoginException.ErrorType.PASSWORD, "Invalid password");
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public UserResponse updateUser(Long id, RegisterRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Copia le proprietà dal request all'utente, escludendo l'id e i ruoli
        BeanUtils.copyProperties(updateRequest, user, "id", "roles");

        // Cripta la password solo se è presente nel request
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(updateRequest.getPassword()));
        }

        // Salva l'utente aggiornato nel repository
        userRepository.save(user);

        // Crea la risposta e copia le proprietà dall'utente aggiornato
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);

        return response;
    }

}
