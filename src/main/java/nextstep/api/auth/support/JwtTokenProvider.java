package nextstep.api.auth.support;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nextstep.api.auth.aop.principal.UserPrincipal;

@Component
public class JwtTokenProvider {
    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") final String secretKey,
                            @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(final String principal, final String role) {
        final var claims = Jwts.claims().setSubject(principal);
        final var now = new Date();
        final var validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public UserPrincipal getUserPrincipal(final String token) {
        final var claims = extractClaims(token);
        final var username = claims.getSubject();
        final var role = claims.get("role", String.class);

        return new UserPrincipal(username, role);
    }

    public boolean validateToken(final String token) {
        try {
            final var claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());

        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractClaims(final String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}

