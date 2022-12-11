package pl.edu.pw.restapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.restapi.dto.ChangePasswordDTO;
import pl.edu.pw.restapi.dto.LoginCredentialsDTO;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;
import pl.edu.pw.restapi.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentialsDTO credentials) {

    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterCredentialsDTO credentials) {
        userService.registerUser(credentials);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/user/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestParam("email") String email) {
        userService.resetPassword(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/user/password/change")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal String username,
                                               @RequestBody @Valid ChangePasswordDTO changePassword) {
        userService.changePassword(username, changePassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
