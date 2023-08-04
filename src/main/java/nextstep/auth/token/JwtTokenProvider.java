package nextstep.auth.token;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    // TODO: 테스트하기 위해 초깃값을 넣어주고 나중에 @Value로 초기화 -> 프로덕션에선 어떻게 Spring과 분리한 POJO 테스트로 만들 수 있을까?
    private String secretKey;
    private long validityInMilliseconds;

    JwtTokenProvider(
        @Value("${security.jwt.token.secret-key}") String secretKey,
        @Value("${security.jwt.token.expire-length}") long validityInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String principal, String role) {
        Claims claims = Jwts.claims().setSubject(principal);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPrincipal(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoles(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

