package atdd.auth.application;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atdd.configure.JwtConfig;
import atdd.user.application.exception.UnauthorizedException;
import atdd.auth.application.dto.AuthInfoView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
  private JwtConfig jwtConfig;
  private SecretKey jwtKey;


  @Autowired
  public AuthService(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
    initJwtKey();
  }

  private void initJwtKey() {
    byte[] rawKey = Decoders.BASE64
      .decode(jwtConfig.getKey());
    jwtKey = Keys.hmacShaKeyFor(rawKey);
  }

  public AuthInfoView generateAuthToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);

    Date now = new Date();
    Date validity = new Date(now.getTime() + AuthConstants.TokenDuration);

    String accessToken = Jwts.builder()
      .setClaims(claims)
      .setIssuedAt(now)
      .setExpiration(validity)
      .signWith(jwtKey)
      .compact();
    return new AuthInfoView(accessToken, AuthConstants.TokenType);
  }

  public String authUser(AuthInfoView authInfoView) {
    Claims claim = Jwts.parser()
      .setSigningKey(jwtKey)
      .parseClaimsJws(authInfoView.getAccessToken())
      .getBody();

    Date now = new Date();
    if(now.compareTo(claim.getExpiration()) > 0) {
      throw new UnauthorizedException();
    }

    return claim.getSubject();
  }
}
