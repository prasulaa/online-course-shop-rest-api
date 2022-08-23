package pl.edu.pw.restapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.restapi.dto.LoginCredentialsDTO;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;
import pl.edu.pw.restapi.service.UserService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentialsDTO credentials) {

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterCredentialsDTO credentials) {
        try {
            userService.registerUser(credentials);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}