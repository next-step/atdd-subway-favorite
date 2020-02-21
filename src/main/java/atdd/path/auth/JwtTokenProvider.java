package atdd.path.auth;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Component()
public class JwtTokenProvider {
    @Autowired
    @Value("${jwt.expireLength}")
    String validityInMilliseconds;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String createToken(String email) {

        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date();
        Long validityInMilliseconds = Long.parseLong(this.validityInMilliseconds, 10);
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity).signWith(key).compact();
    }

    public String parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

}
