package it.epicode.whatsnextbe.controller;

import it.epicode.whatsnextbe.dto.request.login.LoginModel;
import it.epicode.whatsnextbe.dto.request.register.RegisterModel;
import it.epicode.whatsnextbe.dto.request.register.RegisterRequest;
import it.epicode.whatsnextbe.dto.response.login.LoginResponse;
import it.epicode.whatsnextbe.dto.response.register.RegisterResponse;
import it.epicode.whatsnextbe.dto.response.user.UserResponse;
import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.security.ApiValidationException;
import it.epicode.whatsnextbe.service.UserService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        if (userResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<RegisterResponse> registerAdmin(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.registerAdmin(request));
    }

    //POST - LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw  new ApiValidationException(validator.getAllErrors());
        }
        return new ResponseEntity<>(userService.login(model.username(), model.password()).orElseThrow(), HttpStatus.OK);
    }

    // POST - Register User
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Validated RegisterModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        RegisterRequest registerRequest = RegisterRequest.builder()
                .withFirstName(model.firstName())
                .withLastName(model.lastName())
                .withUsername(model.username())
                .withEmail(model.email())
                .withPassword(model.password())
                .build();

        RegisterResponse registeredUser = userService.registerUser(registerRequest);
        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }
}
