package nextstep.auth.model.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰 생성하고 payload 조회")
    void createToken() {
        // given
        String payload = "test";

        // when
        String token = jwtTokenProvider.createToken(payload);

        // then
        assertThat(jwtTokenProvider.getPayload(token)).isEqualTo("test");
    }

    @Test
    @DisplayName("토큰을 검증한다.")
    void validateToken() {
        // given
        String token = jwtTokenProvider.createToken("test");

        // when/then
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        assertThat(jwtTokenProvider.validateToken("esdst")).isFalse();
    }
}