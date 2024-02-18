package nextstep.auth.application;

import nextstep.auth.application.dto.TokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class JwtTokenProviderTest {
    private static final long TOKEN_ID = 1L;
    private static final String TOKEN_EMAIL = "test@test.com";
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider("testSecretKey", 1000 * 10);
        token = jwtTokenProvider.createToken(TOKEN_ID, TOKEN_EMAIL);
    }

    @Test
    void createTokenTest() {
        assertThat(token).isNotBlank();
    }

    @Test
    void getPrincipalTest() {
        final TokenInfo actual = jwtTokenProvider.getPrincipal(token);
        final TokenInfo expected = new TokenInfo(TOKEN_ID, TOKEN_EMAIL);

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void invalidTokenTest() {
        final String invalidToken = "invalid";

        assertSoftly(softly -> {
            softly.assertThat(jwtTokenProvider.validateToken(token)).isTrue();
            softly.assertThat(jwtTokenProvider.validateToken(invalidToken)).isFalse();
        });
    }
}
