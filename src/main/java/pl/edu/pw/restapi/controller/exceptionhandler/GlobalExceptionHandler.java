package pl.edu.pw.restapi.controller.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import pl.edu.pw.restapi.dto.ExceptionDTO;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> handle(IllegalArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(new ExceptionDTO(status, e.getMessage()), status);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> handle(EntityNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(new ExceptionDTO(status), status);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> handle(MethodArgumentTypeMismatchException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = "Wrong argument type";

        return new ResponseEntity<>(new ExceptionDTO(status, msg), status);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> handle(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = e.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(". "));

        return new ResponseEntity<>(new ExceptionDTO(status, msg), status);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> handle(ConversionFailedException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = "Wrong argument value";

        return new ResponseEntity<>(new ExceptionDTO(status, msg), status);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> handle(HttpMessageNotReadableException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = "Wrong json format";

        return new ResponseEntity<>(new ExceptionDTO(status, msg), status);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> handle(MultipartException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = "Wrong file format";

        return new ResponseEntity<>(new ExceptionDTO(status, msg), status);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> handle(HttpRequestMethodNotSupportedException e) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;

        return new ResponseEntity<>(new ExceptionDTO(status), status);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> handle(Exception e) {
        log.error("Unexpected exception occurred", e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(new ExceptionDTO(status), status);
    }

}
