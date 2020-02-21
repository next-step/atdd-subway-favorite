package atdd.path.application;

import atdd.path.TestConstant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JwtTokenProvider.class)
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("토큰 생성")
    @Test
    void createToken() {
        // when
        String token = jwtTokenProvider.createToken(TestConstant.EMAIL_BROWN);

        // then
        assertThat(token).isNotNull();
    }

    @DisplayName("생성된 토큰에서 유저 이메일 가져온다")
    @Test
    void getUserEmail() {
        // given
        String token = jwtTokenProvider.createToken(TestConstant.EMAIL_BROWN);

        // when
        String email = jwtTokenProvider.getUserEmail(String.format("Bearer %s", token));

        // then
        assertThat(email).isEqualTo(TestConstant.EMAIL_BROWN);
    }

    @DisplayName("토큰 유효성 체크한다")
    @Test
    void validateToken() {
        // given
        String token = jwtTokenProvider.createToken(TestConstant.EMAIL_BROWN);

        // when
        Boolean valid = jwtTokenProvider.validateToken(String.format("Bearer %s", token));

        // then
        assertThat(valid).isEqualTo(true);
    }

}
