package nextstep.login.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import nextstep.common.auth.InvalidTokenException;
import nextstep.common.auth.MemberPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private static final String CLAIM_NAME_ROLES = "roles";
    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") final String secretKey,
            @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(final String principal, final List<String> roles) {
        Claims claims = Jwts.claims().setSubject(principal);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim(CLAIM_NAME_ROLES, roles)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public MemberPayload decodeToken(final String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException();
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return new MemberPayload(claims.getSubject(), claims.get(CLAIM_NAME_ROLES, List.class));
        } catch (SignatureException exception) {
            throw new InvalidTokenException();
        }
    }

    public String getPrincipal(final String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> getRoles(final String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get(CLAIM_NAME_ROLES, List.class);
    }

    public boolean validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
