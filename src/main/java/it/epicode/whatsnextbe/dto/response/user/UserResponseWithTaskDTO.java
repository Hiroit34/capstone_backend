package it.epicode.whatsnextbe.dto.response.user;

import it.epicode.whatsnextbe.dto.response.task.TaskResponseTitleAndID;
import it.epicode.whatsnextbe.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseWithTaskDTO {
    private Long id;
    private String username;
    private String email;
    private String roles;
    private List<TaskResponseTitleAndID> tasks;
}
