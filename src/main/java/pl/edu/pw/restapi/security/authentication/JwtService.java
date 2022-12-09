package pl.edu.pw.restapi.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final long expirationTime;
    private Set<String> tokenBlacklist;
    private final String secret;

    public JwtService(@Value("${jwt.expirationTime}") long expirationTime,
                      @Value("${jwt.secret}") String secret,
                      Set<String> blacklistToken) {
        this.expirationTime = expirationTime;
        this.secret = secret;
        this.tokenBlacklist = blacklistToken;
    }

    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
    }

    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
    }

    public String getSubject(String token) {
        deleteExpiredTokens();
        if (token == null || isTokenBlacklisted(token)) {
            return null;
        }
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getSubject();
    }

    private void deleteExpiredTokens() {
        tokenBlacklist = tokenBlacklist.stream()
                .filter((token) -> !isTokenExpired(token))
                .collect(Collectors.toSet());
    }

    private boolean isTokenExpired(String token) {
        return JWT.decode(token)
                .getExpiresAt()
                .before(new Date());
    }

    private boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

}
