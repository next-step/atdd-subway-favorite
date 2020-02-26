package atdd.path.application;

import atdd.user.application.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JwtUtils.class)
public class JwtUtilsTest {
    @Autowired
    private JwtUtils jwtUtils;

    @DisplayName("토큰 생성 테스트")
    @Test
    public void createToken() {
        String accessToken = jwtUtils.createToken("test123@gmail.com");

        assertThat(accessToken.length()).isGreaterThan(0);
    }

    @DisplayName("토큰 검증 테스트")
    @Test
    public void verify() {
        String accessToken = jwtUtils.createToken("test123@gmail.com");

        boolean verify = jwtUtils.verify(accessToken);

        assertThat(verify).isEqualTo(true);
    }

    @DisplayName("토큰 정보 가져오기")
    @Test
    public void tokenClaims() {
        final String email = "test123@gmail.com";
        String accessToken = jwtUtils.createToken(email);

        Claims claims = jwtUtils.tokenClaims(accessToken);

        assertThat(claims.get("email")).isEqualTo(email);
    }


}
