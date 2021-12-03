package ru.erminson.lc.model.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus httpStatus, Throwable ex) {
        this(httpStatus);
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }
}
