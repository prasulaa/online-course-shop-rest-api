package pl.edu.pw.restapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ExceptionDTO {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private String message;

    public ExceptionDTO(HttpStatus status) {
        timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.toString();
    }

    public ExceptionDTO(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

}
