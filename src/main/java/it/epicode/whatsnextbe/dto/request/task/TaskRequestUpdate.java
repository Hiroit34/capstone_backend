package it.epicode.whatsnextbe.dto.request.task;

import it.epicode.whatsnextbe.dto.request.category.CategoryRequest;
import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.model.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestUpdate {

    @NotEmpty(message = "Il titolo è obbligatorio")
    @Size(max = 50, message = "Il titolo deve avere un massimo di 50 caratteri")
    private String title;

    @Size(max = 50, message = "La descrizione deve avere un massimo di 50 caratteri")
    private String description;

    @NotNull(message = "Lo status è obbligatorio")
    private Status.StatusType status;

    @NotNull(message = "La categoria è obbligatoria")
    private CategoryRequest category;

    @NotEmpty(message = "Gli utenti assegnati sono obbligatori")
    private List<Long> userIds;

}
