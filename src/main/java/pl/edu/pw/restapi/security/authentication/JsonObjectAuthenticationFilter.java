package pl.edu.pw.restapi.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.edu.pw.restapi.dto.LoginCredentialsDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class JsonObjectAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginCredentialsDTO authRequest = objectMapper.readValue(request.getInputStream(), LoginCredentialsDTO.class);
            UsernamePasswordAuthenticationToken token = createToken(authRequest);

            setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new CustomAuthenticationException("Wrong data format");
        }
    }

    private UsernamePasswordAuthenticationToken createToken(LoginCredentialsDTO authRequest) {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new BadCredentialsException("Username and password cannot be empty");
        } else {
            return new UsernamePasswordAuthenticationToken(username, password);
        }
    }

}
