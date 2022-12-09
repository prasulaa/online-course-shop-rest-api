package pl.edu.pw.restapi.security.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.edu.pw.restapi.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UserService userService;
    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtService jwtService,
                                  UserService userDetailsService) {
        super(authenticationManager);
        this.userService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        try {
            String token = request.getHeader(TOKEN_HEADER);
            if (token != null && token.startsWith(TOKEN_PREFIX)) {
                return getAuthentication(token.replace(TOKEN_PREFIX, ""));
            } else {
                Optional<Cookie> authCookie = Arrays.stream(request.getCookies())
                        .filter(c -> c.getName().equals(TOKEN_HEADER))
                        .findFirst();

                if (authCookie.isPresent()) {
                    return getAuthentication(authCookie.get().getValue());
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = jwtService.getSubject(token);

        if (username != null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
        }

        return null;
    }

}
