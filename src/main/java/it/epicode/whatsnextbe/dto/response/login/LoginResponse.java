package it.epicode.whatsnextbe.dto.response.login;

import it.epicode.whatsnextbe.dto.response.register.RegisterResponse;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponse {
    RegisterResponse user;
    String token;

    @Builder(setterPrefix = "with")
    public LoginResponse(RegisterResponse user, String token) {
        this.user = user;
        this.token = token;
    }
}
