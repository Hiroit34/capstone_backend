package it.epicode.whatsnextbe.dto.response.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseTitleAndID {
    private Long id;
    private String title;
}
