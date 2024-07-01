package it.epicode.whatsnextbe.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseLight {
    private Long id;
    private String username;
    private String email;
}
