package it.epicode.whatsnextbe.dto.request.task;

import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.model.Status;
import it.epicode.whatsnextbe.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TaskRequest {


    @NotEmpty(message = "Il titolo è obbligatorio")
    @Size(max = 50, message = "Il titolo deve avere un massimo di 50 caratteri")
    private String title;

    @Size(max = 255, message = "La descrizione deve avere un massimo di 255 caratteri")
    private String description;

    @NotNull(message = "Lo status è obbligatorio")
    private Status.StatusType status;

    @NotNull(message = "La categoria è obbligatoria")
    private Category category;

    @NotEmpty(message = "Devi assegnare la task ad almeno uno user")
    private List<Long> userIds;

}
