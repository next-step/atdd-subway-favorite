package atdd.path.application.provider;

import atdd.path.application.exception.InvalidJwtAuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

import static atdd.path.TestConstant.TEST_MEMBER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(classes = JwtTokenProvider.class)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${spring.security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @DisplayName("토큰을 생성 할 수 있다")
    @Test
    void beAbleToCreateToken() {
        String token = jwtTokenProvider.createToken(TEST_MEMBER_EMAIL);
        String[] tokens = (StringUtils.isEmpty(token)) ? null : token.split("\\.");

        assertThat(tokens).isNotNull();
        assertThat(tokens.length).isEqualTo(3);
        assertThat(tokens[0]).isEqualTo("eyJhbGciOiJIUzI1NiJ9"); // HS256
    }

    @DisplayName("토큰 유효성 검사를 성공 할 수 있다")
    @Test
    void beAbleToValidateTokenSuccessful() {
        String token = jwtTokenProvider.createToken(TEST_MEMBER_EMAIL);

        boolean isValidateToken = jwtTokenProvider.validateToken(token);

        assertThat(isValidateToken).isTrue();
    }

    @DisplayName("토큰 유효기간이 만료될 수 있다")
    @Test
    void beAbleToValidateTokenExpire() throws InterruptedException {
        String token = jwtTokenProvider.createToken(TEST_MEMBER_EMAIL);

        TimeUnit.MILLISECONDS.sleep(validityInMilliseconds);

        assertThrows(
                InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.validateToken(token),
                "Expired or invalid JWT token"
        );
    }

    @DisplayName("토큰으로 이메일 주소를 가져올 수 있다")
    @Test
    void beAbleToExtractEmailFromToken() {
        String token = jwtTokenProvider.createToken(TEST_MEMBER_EMAIL);
        String email = jwtTokenProvider.getUserEmail(token);

        assertThat(email).isEqualTo(TEST_MEMBER_EMAIL);
    }

}