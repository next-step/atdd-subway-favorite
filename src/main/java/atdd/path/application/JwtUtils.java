package atdd.path.application;

import atdd.path.application.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtils {
    @Value("${authentication.jwt.secret}")
    private String secret;

    @Value("${authentication.jwt.expirationPeriod}")
    private long expirationPeriod;

    public String createToken(final String email) {
        long currentTimeMillis = System.currentTimeMillis() + expirationPeriod;
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        return Jwts.builder()
                .setExpiration(new Date(currentTimeMillis))
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean verify(String token) {
        try {
            tokenClaims(token);

            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    public Claims tokenClaims(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }
}
