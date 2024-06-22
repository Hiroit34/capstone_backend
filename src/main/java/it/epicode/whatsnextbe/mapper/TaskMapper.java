package it.epicode.whatsnextbe.mapper;

import it.epicode.whatsnextbe.dto.response.task.TaskResponseTitleAndID;
import it.epicode.whatsnextbe.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponseTitleAndID convertToTaskLightDTO(Task task) {
        TaskResponseTitleAndID dto = new TaskResponseTitleAndID();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        return dto;
    }

}
