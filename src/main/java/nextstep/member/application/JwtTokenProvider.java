package nextstep.member.application;

import io.jsonwebtoken.*;
import java.util.Date;
import nextstep.member.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  @Value("${security.jwt.token.secret-key}")
  private String secretKey;

  @Value("${security.jwt.token.expire-length}")
  private long validityInMilliseconds;

  public String createToken(String principal) {
    Claims claims = Jwts.claims().setSubject(principal);
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public String getPrincipal(String token) {
    try {
      return getPayload(token).getSubject();
    } catch (JwtException | IllegalArgumentException e) {
      throw new AuthenticationException();
    }
  }

  private Claims getPayload(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  public boolean validateToken(String token) {
    try {
      return !getPayload(token).getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}
