package it.epicode.whatsnextbe.dto.request.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RegisterRequest {
    String firstName;
    String lastName;
    String username;
    String email;
    String password;
//    String avatar;
}
