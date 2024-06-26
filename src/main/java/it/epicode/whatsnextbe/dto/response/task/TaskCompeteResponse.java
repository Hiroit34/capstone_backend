package it.epicode.whatsnextbe.dto.response.task;

import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.model.Status;
import it.epicode.whatsnextbe.model.User;
import lombok.Data;

import java.util.List;

@Data
public class TaskCompeteResponse {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Category category;
    private List<User> users;
}
