package it.epicode.whatsnextbe.dto.response.user;

import it.epicode.whatsnextbe.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private List<Role> roles;
}
