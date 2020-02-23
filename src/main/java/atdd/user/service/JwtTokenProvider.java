package atdd.user.service;

import atdd.user.dto.JwtTokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtTokenInfo jwtTokenInfo;

    public JwtTokenProvider(JwtTokenInfo jwtTokenInfo) {
        this.jwtTokenInfo = jwtTokenInfo;
    }

    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        final Date nowDate = new Date();
        final Date expiration = new Date(nowDate.getTime() + jwtTokenInfo.getExpireLength());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtTokenInfo.getSecretKey())
                .compact();
    }

}
