package nextstep.subway.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class JwtPracticeTest {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    public void setup(){
       token = jwtTokenProvider.createToken("토큰생성");
    }

    @Test
    public void checkedHeader(){
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

        assertThat(claims.getHeader().get("alg")).isEqualTo(SignatureAlgorithm.HS256.getValue());
    }

    @Test
    public void checkedExpirateDate(){
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

        assertThat(claims.getBody().getIssuedAt()).isBefore(claims.getBody().getExpiration());
    }

}
