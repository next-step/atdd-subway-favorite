package atdd.path.application;

import atdd.path.application.exception.InvalidJwtAuthenticationException;
import io.jsonwebtoken.*;
import jdk.internal.joptsimple.internal.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-time-seconds}")
    private long validityInSeconds;

    private static final String JWT_HEADER_AUTHORIZATION = "Authorization";
    private static final String JWT_TOKEN_TYPE = "Bearer ";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * 토큰 생성
     * @param email
     * @return
     */
    public String createToken(String email) {

        Claims claims = Jwts.claims().setSubject(email);

        Date validity = Date.from(LocalDateTime.now().plusSeconds(validityInSeconds).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 토큰에서 값(email) 추출
     * @param token
     * @return
     */
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader(JWT_HEADER_AUTHORIZATION))
                .orElse(Strings.EMPTY)
                .replaceAll(JWT_TOKEN_TYPE, "");
    }

    /**
     * 토큰 유효성 검사
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }
}