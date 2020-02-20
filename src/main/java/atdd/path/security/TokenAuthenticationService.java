package atdd.path.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

import static atdd.path.dao.UserDao.EMAIL_KEY;

@Component
public class TokenAuthenticationService {
    private static final String SALT = "63B75D39E3F6BFE72263F7C1145AC22E";
    private static final String HEADER_STRING = "Authorization";


    public byte[] generateKey(String salt) {
        return salt.getBytes();
    }

    public String toJwtByEmail(String email) {
        return Jwts.builder()
                .claim(EMAIL_KEY, email)
                .setHeaderParam("tokenType", "Bearer")
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, this.generateKey(SALT))
                .compact();
    }

    public Jws<Claims> getJwtClaim(String jwt) {
        return Jwts.parser()
                .setSigningKey(generateKey(SALT))
                .parseClaimsJws(jwt);
    }

    public String getEmailByClaims(Jws<Claims> claims) {
        return claims.getBody().get(EMAIL_KEY).toString();
    }
}
