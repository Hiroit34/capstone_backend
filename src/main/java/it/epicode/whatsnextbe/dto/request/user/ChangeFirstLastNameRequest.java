package it.epicode.whatsnextbe.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeFirstLastNameRequest {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 50, message = "Il nome deve essere inferiore a 50 caratteri")
    private String firstName;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(max = 50, message = "Il cognome deve essere inferiore a 50 caratteri")
    private String lastName;

}
