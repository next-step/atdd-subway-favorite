package nextstep.auth.token;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtTokenProperties jwtTokenProperties;

    public JwtTokenProvider(JwtTokenProperties jwtTokenProperties) {
        this.jwtTokenProperties = jwtTokenProperties;
    }

    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtTokenProperties.getValidityInMilliseconds());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, jwtTokenProperties.getSecretKey())
                .compact();
    }

    public String getPayload(String token) {
        return Jwts.parser()
                .setSigningKey(jwtTokenProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(jwtTokenProperties.getSecretKey())
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

