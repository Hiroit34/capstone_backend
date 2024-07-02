package it.epicode.whatsnextbe.mapper;

import it.epicode.whatsnextbe.dto.response.category.CategoryWithTaskResponse;
import it.epicode.whatsnextbe.dto.response.task.TaskResponseTitleAndID;
import it.epicode.whatsnextbe.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public static CategoryWithTaskResponse mapToCategoryWithTaskResponse(Category category) {
        List<TaskResponseTitleAndID> tasks = category.getTasks().stream()
                .map(task -> new TaskResponseTitleAndID(task.getId(), task.getTitle()))
                .collect(Collectors.toList());

        CategoryWithTaskResponse response = new CategoryWithTaskResponse();
        response.setId(category.getId());
        response.setCategoryType(category.getCategoryType());
        response.setTasks(tasks);
        return response;
    }
}
