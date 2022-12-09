package pl.edu.pw.restapi.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final long expirationTime;
    private final JwtService jwtService;

    public RestAuthenticationSuccessHandler(@Value("${jwt.expirationTime}") long expirationTime,
                                            JwtService jwtService) {
        this.expirationTime = expirationTime;
        this.jwtService = jwtService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtService.createToken(principal.getUsername());
        response.getOutputStream().print("{\"token\": \"" + token + "\"}");
        response.addCookie(authCookie(token));
        response.addCookie(infoCookie());
    }

    private Cookie authCookie(String token) {
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) expirationTime / 1000);
        return cookie;
    }

    private Cookie infoCookie() {
        Cookie cookie = new Cookie("AuthInfo", "");
        cookie.setPath("/");
        cookie.setMaxAge((int) expirationTime / 1000);
        return cookie;
    }

}
