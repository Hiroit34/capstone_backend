package it.epicode.whatsnextbe.dto.response.category;

import it.epicode.whatsnextbe.dto.response.task.TaskResponse;
import it.epicode.whatsnextbe.dto.response.task.TaskResponseTitleAndID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithTaskResponse {

    private Long id;
    private String categoryType;
    private String description;
    private List<TaskResponseTitleAndID> task;

}
