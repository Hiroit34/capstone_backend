package it.epicode.whatsnextbe.dto.response.task;


import it.epicode.whatsnextbe.dto.response.user.UserResponseLight;
import lombok.Data;

import java.util.List;

@Data
public class TaskResponseLight {
    private Long id;
    private String name;
    private String description;
    private String status;
    private List<UserResponseLight> users;
}
