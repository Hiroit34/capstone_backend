package it.epicode.whatsnextbe.dto.request.status;

import it.epicode.whatsnextbe.model.Status;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusTaskRequest {

    @NotEmpty(message = "Il tipo di stato non puo essere vuoto")
    private String status;

}
