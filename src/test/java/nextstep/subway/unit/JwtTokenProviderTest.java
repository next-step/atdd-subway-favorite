package nextstep.subway.unit;

import nextstep.auth.application.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenProviderTest extends SpringTest {

    private String token;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.token = jwtTokenProvider.createToken("username", List.of("ROLE_MEMBER"));
    }

    @DisplayName("인증 토큰 생성")
    @Test
    void createToken() {
        // when
        final String token = jwtTokenProvider.createToken("username", List.of("ROLE_MEMBER"));

        // then
        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo("username");
        assertThat(jwtTokenProvider.getRoles(token)).isEqualTo(List.of("ROLE_MEMBER"));
    }

    @DisplayName("Principal 조회")
    @Test
    void getPrincipal() {
        // when
        final String principal = jwtTokenProvider.getPrincipal(this.token);

        // then
        assertThat(principal).isEqualTo("username");
    }

    @DisplayName("유저 roles 조회")
    @Test
    void getRoles() {
        // when
        final List<String> roles = jwtTokenProvider.getRoles(this.token);

        // then
        assertThat(roles).containsOnly("ROLE_MEMBER");
    }

    @DisplayName("토큰 인증")
    @Test
    void validateToken() {
        // when
        final boolean isValidToken = jwtTokenProvider.validateToken(this.token);

        // then
        assertThat(isValidToken).isTrue();
    }
}
