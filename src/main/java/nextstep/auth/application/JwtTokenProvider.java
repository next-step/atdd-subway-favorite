package nextstep.auth.application;

import io.jsonwebtoken.*;
import nextstep.auth.application.dto.TokenInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String EMAIL_KEY = "email";
    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") final String secretKey,
            @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(final Long id, final String email) {
        final Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put(EMAIL_KEY, email);
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public TokenInfo getPrincipal(final String token) {
        final Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return new TokenInfo(Long.parseLong(body.getSubject()), body.get(EMAIL_KEY, String.class));
    }

    public boolean validateToken(final String token) {
        try {
            final Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

