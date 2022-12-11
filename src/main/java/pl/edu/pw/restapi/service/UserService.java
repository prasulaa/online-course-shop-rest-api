package pl.edu.pw.restapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.edu.pw.restapi.dto.RegisterCredentialsDTO;

public interface UserService extends UserDetailsService {

    void registerUser(RegisterCredentialsDTO credentials);

    void resetPassword(String email);
}
