package it.epicode.whatsnextbe.mapper;

import it.epicode.whatsnextbe.dto.response.user.UserResponseLight;
import it.epicode.whatsnextbe.dto.response.user.UserResponseWithTaskDTO;
import it.epicode.whatsnextbe.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final TaskMapper taskMapper;

    public UserResponseWithTaskDTO convertToUserResponseDTO(User user) {
        UserResponseWithTaskDTO dto = new UserResponseWithTaskDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().toString());
        dto.setTasks(user.getTasks().stream()
                .map(taskMapper::convertToTaskLightDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public static UserResponseLight convertToUserResponseLight(User user) {
        UserResponseLight dto = new UserResponseLight();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
