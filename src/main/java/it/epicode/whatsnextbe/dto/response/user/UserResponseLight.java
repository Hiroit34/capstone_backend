package it.epicode.whatsnextbe.dto.response.user;

import lombok.Data;
import lombok.Setter;

@Data
public class UserResponseLight {
    private Long id;
    private String username;
    private String email;
}
