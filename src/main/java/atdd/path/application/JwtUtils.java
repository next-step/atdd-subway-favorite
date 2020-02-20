package atdd.path.application;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${authentication.jwt.secret}")
    private String secret;

    @Value("${authentication.jwt.expirationPeriod}")
    private long expirationPeriod;

    public String createToken(final String name) {
        long currentTimeMillis = System.currentTimeMillis() + expirationPeriod;

        return JWT.create()
                .withExpiresAt(new Date(currentTimeMillis))
                .withClaim("name", name)
                .sign(Algorithm.HMAC256(secret));
    }

    public String verify(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("name").toString();
    }
}
