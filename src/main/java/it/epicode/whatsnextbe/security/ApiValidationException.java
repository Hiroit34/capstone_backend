package it.epicode.whatsnextbe.security;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.io.Serial;
import java.util.List;
import java.util.stream.Collectors;

public class ApiValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<ObjectError> errorsList;
    private final HttpStatus status;

    public ApiValidationException(List<ObjectError> errorsList) {
        super("Validation errors occurred: " + formatErrors(errorsList));
        this.errorsList = errorsList;
        this.status = HttpStatus.SERVICE_UNAVAILABLE;
    }

    public ApiValidationException(List<ObjectError> errorsList, HttpStatus status) {
        super("Validation errors occurred: " + formatErrors(errorsList));
        this.errorsList = errorsList;
        this.status = status;
    }

    public ApiValidationException(String message, List<ObjectError> errorsList, HttpStatus status) {
        super(message + ": " + formatErrors(errorsList));
        this.errorsList = errorsList;
        this.status = status;
    }

    private static String formatErrors(List<ObjectError> errorsList) {
        return errorsList.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }

    public List<ObjectError> getErrorsList() {
        return errorsList;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
