package pl.edu.pw.restapi.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.edu.pw.restapi.dto.ExceptionDTO;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@Component
public class FailureAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final static HttpStatus RESPONSE_STATUS = HttpStatus.UNAUTHORIZED;

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(RESPONSE_STATUS.value());
        writeFailureResponseBody(response.getOutputStream());
    }

    private void writeFailureResponseBody(ServletOutputStream outputStream) throws IOException {
        String responseBody = objectMapper.writeValueAsString(getException());
        outputStream.print(responseBody);
        outputStream.flush();
    }

    private ExceptionDTO getException() {
        String message = "Wrong username or password";
        return new ExceptionDTO(RESPONSE_STATUS, message);
    }

}
