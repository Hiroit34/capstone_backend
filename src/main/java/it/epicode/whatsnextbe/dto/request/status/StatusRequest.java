package it.epicode.whatsnextbe.dto.request.status;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusRequest {

    @NotEmpty(message = "Il tipo di stato non puo essere vuoto")
    private String status;

}
