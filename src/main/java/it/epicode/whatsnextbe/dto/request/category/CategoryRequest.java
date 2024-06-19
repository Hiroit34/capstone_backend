package it.epicode.whatsnextbe.dto.request.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CategoryRequest {

    @NotEmpty(message = "La tipo di categoria non puo essere vuoto")
    private String categoryType;

}
