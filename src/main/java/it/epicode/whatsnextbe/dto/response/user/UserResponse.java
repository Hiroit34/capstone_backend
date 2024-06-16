package it.epicode.whatsnextbe.dto.response.user;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
}
