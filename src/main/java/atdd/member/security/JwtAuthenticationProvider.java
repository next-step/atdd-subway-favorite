package atdd.member.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider {

    private static final long JWT_TOKEN_VALIDITY = 60 * 60 * 24;

    @Value("${security.jwt.token.secret-key}")
    private String secret;

    public String create(String email) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plus(Duration.of(JWT_TOKEN_VALIDITY, ChronoUnit.MINUTES));
        return Jwts.builder()
            .setClaims(Jwts.claims().setSubject(email))
            .setIssuedAt(asDate(now))
            .setExpiration(asDate(validity))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
        return resolver.apply(claims);
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean isExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Date asDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


}
