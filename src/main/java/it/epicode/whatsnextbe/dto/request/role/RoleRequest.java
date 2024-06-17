package it.epicode.whatsnextbe.dto.request.role;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleRequest {

    @NotEmpty(message = "Il tipo di ruolo non puo essere vuoto")
    private String typeRole;

}
