package it.epicode.whatsnextbe.dto.request.user;

import it.epicode.whatsnextbe.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 50, message = "Il nome deve essere inferiore a 50 caratteri")
    private String firstName;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(max = 50, message = "Il cognome deve essere inferiore a 50 caratteri")
    private String lastName;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email deve essere valida")
    @Size(max = 50, message = "L'email deve essere inferiore a 50 caratteri")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(max = 50, message = "La password deve essere inferiore a 50 caratteri")
    private String password;

    @NotBlank(message = "Il nome utente è obbligatorio")
    @Size(max = 50, message = "Il nome utente deve essere inferiore a 50 caratteri")
    private String userName;

    @NotBlank(message = "Il ruolo è obbligatorio")
    private String role;

}
