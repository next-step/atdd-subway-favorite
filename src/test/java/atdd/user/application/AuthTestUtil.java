package atdd.user.application;

import javax.crypto.SecretKey;

import atdd.configure.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class AuthTestUtil {
  public Claims ParseAuthToken(JwtConfig jwtConfig, String authTokenString) {
    byte[] rawKey = Decoders.BASE64
      .decode(jwtConfig.getKey());
    SecretKey key = Keys.hmacShaKeyFor(rawKey);

    return Jwts.parser()
        .setSigningKey(key)
        .parseClaimsJws(authTokenString)
        .getBody();
  }

  public AuthTestUtil() {
  }
}
