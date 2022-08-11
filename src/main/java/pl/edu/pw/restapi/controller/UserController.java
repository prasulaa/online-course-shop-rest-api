package pl.edu.pw.restapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pw.restapi.security.LoginCredentials;

@RestController
public class UserController {

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentials credentials) {

    }

}
