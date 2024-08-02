package nextstep.member.infrastructure.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.entity.TokenPrincipal;
import nextstep.member.domain.command.TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenGenerator {

    private final JwtConfig jwtConfig;
    private static String CLAIMS_EMAIL = "EMAIL";

    public String createToken(TokenPrincipal principal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIMS_EMAIL, principal.getEmail());

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtConfig.getValidityInMilliseconds());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(principal.getSubject().toString())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();
    }

    public TokenPrincipal getPrincipal(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtConfig.getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        return new TokenPrincipal(Long.valueOf(claims.getSubject()), claims.get(CLAIMS_EMAIL, String.class));
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

