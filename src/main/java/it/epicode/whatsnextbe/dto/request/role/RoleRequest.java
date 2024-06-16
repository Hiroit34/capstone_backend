package it.epicode.whatsnextbe.dto.request.role;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleRequest {

    @NotEmpty(message = "Il tipo di ruolo non puo essere vuoto")
    private String typeRole;

}
