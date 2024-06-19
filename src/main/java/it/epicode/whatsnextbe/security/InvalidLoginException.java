package it.epicode.whatsnextbe.security;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidLoginException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public enum ErrorType {
        USERNAME, PASSWORD
    }

    private final String username;
    private final String password;
    private final ErrorType errorType;

    public InvalidLoginException(String username, String password, ErrorType errorType, String message) {
        super(message);
        this.username = username;
        this.password = password;
        this.errorType = errorType;
    }

    public InvalidLoginException(String username, String password, ErrorType errorType) {
        this(username, password, errorType, "Invalid credentials");
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
