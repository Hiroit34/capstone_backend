package it.epicode.whatsnextbe.dto.response.register;

import it.epicode.whatsnextbe.model.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RegisterResponse {

    Long id;
    String firstName;
    String lastName;
    String email;
    String username;
    private List<Role> roles;

    @Builder(setterPrefix = "with")
    public RegisterResponse(Long id, String firstName, String lastName, String email, String username, List<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }
}
