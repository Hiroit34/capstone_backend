package it.epicode.whatsnextbe.dto.response.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.whatsnextbe.dto.response.user.UserResponseLight;
import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.model.Status;
import it.epicode.whatsnextbe.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Category category;

    public TaskResponse(Status status) {
        this.status = status.toString();
    }
}
