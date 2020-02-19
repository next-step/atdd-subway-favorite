package atdd.auth.application;

import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atdd.configure.JwtConfig;
import atdd.auth.application.dto.AuthInfoView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

  private JwtConfig jwtConfig;
  private SecretKey jwtKey;

  private static final String tokenType = "bearer";
  private static final Long tokenDuration = 30 * 60 * 1000L;

  public AuthInfoView GenerateAuthToken(String email) {
    if(jwtKey == null){
      InitJwtKey();
    }
    Claims claims = Jwts.claims().setSubject(email);

    Date now = new Date();
    Date validity = new Date(now.getTime() + tokenDuration);

    String accessToken = Jwts.builder()
      .setClaims(claims)
      .setIssuedAt(now)
      .setExpiration(validity)
      .signWith(jwtKey)
      .compact();
    return new AuthInfoView(accessToken, tokenType);
  }

  public Optional<String> AuthUser(AuthInfoView authInfoView) {
    Claims claim = Jwts.parser()
      .setSigningKey(jwtKey)
      .parseClaimsJws(authInfoView.getAccessToken())
      .getBody();

    Date now = new Date();
    if(now.compareTo(claim.getExpiration()) > 0) {
      return Optional.empty();
    }

    return Optional.of(claim.getSubject());
  }

  public AuthService() {
  }

  @Autowired
  public AuthService(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  private void InitJwtKey() {
    byte[] rawKey = Decoders.BASE64
      .decode(jwtConfig.getKey());
    jwtKey = Keys.hmacShaKeyFor(rawKey);
  }
}
