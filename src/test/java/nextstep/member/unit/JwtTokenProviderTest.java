package nextstep.member.unit;

import nextstep.member.application.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final String EMAIL = "email";
    private final List<String> ROLES = List.of("ROLE_MEMBER");

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        // when
        final String token = jwtTokenProvider.createToken(EMAIL, ROLES);

        // then
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("토큰 유효성 검사")
    void tokenValidation() {
        // given
        final String token = jwtTokenProvider.createToken(EMAIL, ROLES);

        // when
        // then
        assertThat(jwtTokenProvider.validateToken("invalidToken")).isFalse();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }
}
