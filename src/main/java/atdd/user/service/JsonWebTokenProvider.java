package atdd.user.service;

import atdd.user.dto.JsonWebTokenInfo;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JsonWebTokenProvider {

    private final JsonWebTokenInfo jsonWebTokenInfo;

    public JsonWebTokenProvider(JsonWebTokenInfo jsonWebTokenInfo) {
        this.jsonWebTokenInfo = jsonWebTokenInfo;
    }

    public String createToken(String email, Date nowDate) {
        Claims claims = Jwts.claims().setSubject(email);
        final Date expiration = new Date(nowDate.getTime() + jsonWebTokenInfo.getExpireLength());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jsonWebTokenInfo.getSecretKey())
                .compact();
    }

    public boolean isExpiredToken(String token, Date nowDate) {
        final Jws<Claims> claimsJws = createClaimsJws(token);

        final Date expiration = claimsJws.getBody().getExpiration();
        final long expirationTime = expiration.getTime();
        final long nowDateTime = nowDate.getTime();
        return expirationTime < nowDateTime;
    }

    public Date getExpiration(String token) {
        return createClaimsJws(token).getBody().getExpiration();
    }

    private Jws<Claims> createClaimsJws(String token) {
        return Jwts.parser()
                .setSigningKey(jsonWebTokenInfo.getSecretKey())
                .parseClaimsJws(token);
    }

    public String parseEmail(String token) {
        return createClaimsJws(token).getBody().getSubject();
    }

}
