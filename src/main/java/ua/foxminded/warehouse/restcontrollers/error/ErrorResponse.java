package ua.foxminded.warehouse.restcontrollers.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;

    public ErrorResponse(HttpStatus status) {
        this.status = status;
    }

    public ErrorResponse(HttpStatus status, LocalDateTime timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    public ErrorResponse() {
        timestamp = LocalDateTime.now();
    }

    public ErrorResponse(HttpStatus status, LocalDateTime timestamp, String message) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }
}
