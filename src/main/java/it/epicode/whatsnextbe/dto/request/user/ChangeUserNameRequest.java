package it.epicode.whatsnextbe.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeUserNameRequest {

    @NotBlank(message = "Il nome utente è obbligatorio")
    @Size(max = 50, message = "Il nome utente deve essere inferiore a 50 caratteri")
    private String userName;

}
