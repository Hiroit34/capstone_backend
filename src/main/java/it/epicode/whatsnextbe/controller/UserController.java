package it.epicode.whatsnextbe.controller;

import it.epicode.whatsnextbe.dto.request.login.LoginModel;
import it.epicode.whatsnextbe.dto.request.register.RegisterModel;
import it.epicode.whatsnextbe.dto.request.register.RegisterRequest;
import it.epicode.whatsnextbe.dto.response.login.LoginResponse;
import it.epicode.whatsnextbe.dto.response.register.RegisterResponse;
import it.epicode.whatsnextbe.dto.response.user.UserResponse;
import it.epicode.whatsnextbe.dto.response.user.UserResponseWithTaskDTO;
import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.error.ApiValidationException;
import it.epicode.whatsnextbe.service.AuthenticationSerivce;
import it.epicode.whatsnextbe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {


    private final UserService userService;
    private final AuthenticationSerivce authenticationSerivce;


    // GET ALL /api/user
    @GetMapping
    public ResponseEntity<List<UserResponseWithTaskDTO>> getAllUser() {
        List<UserResponseWithTaskDTO> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    // GET BY ID /api/user/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        if (userResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponse);
    }

    // POST - REGISTER ADMIN /api/user/registerAdmin
    @PostMapping("/registerAdmin")
    public ResponseEntity<RegisterResponse> registerAdmin(@RequestBody @Validated RegisterRequest request){
        return ResponseEntity.ok(userService.registerAdmin(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Validated RegisterRequest request){
        return ResponseEntity.ok(userService.registerUser(request));
    }

    //POST - LOGIN /api/user/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw  new ApiValidationException(validator.getAllErrors());
        }
        System.out.println("Tentativo di login per l'utente: " + model.username());
        return new ResponseEntity<>(userService.login(model.username(), model.password()).orElseThrow(), HttpStatus.OK);
    }

    // PATCH - Update User /api/user/id
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody @Validated RegisterModel model, BindingResult validator, Principal principal) {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Check if the user is admin or trying to update their own profile using userId
        if (!currentUser.getId().equals(id) && !authenticationSerivce.isAdmin(currentUser.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        RegisterRequest updateRequest = RegisterRequest.builder()
                .withFirstName(model.firstName())
                .withLastName(model.lastName())
                .withUsername(model.username())
                .withEmail(model.email())
                .withPassword(model.password())
                .build();

        UserResponse updatedUser = userService.updateUser(id, updateRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Principal principal) {
        // Ottieni l'utente corrente dal contesto di sicurezza
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        // Controlla se l'utente è admin o sta tentando di eliminare il proprio profilo
        if (!currentUser.getId().equals(id) && !authenticationSerivce.isAdmin(currentUser.getId())) {
            return new ResponseEntity<>("You do not have permission to delete this user.", HttpStatus.FORBIDDEN);
        }
        // Elimina l'utente
        String responseMessage = userService.deleteUser(id);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
