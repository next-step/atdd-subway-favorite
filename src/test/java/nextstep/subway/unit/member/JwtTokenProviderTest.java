package nextstep.subway.unit.member;

import nextstep.member.application.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JWT Token 테스트")
class JwtTokenProviderTest {

    private static final String BEARER = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @DisplayName("Null 또는 비어있는 문자열인 경우에 파싱을 하면 예외가 발생한다")
    @ParameterizedTest(name = "입력값: {0}")
    @NullAndEmptySource
    void parseJwtException(String header) {
        assertThatThrownBy(() -> jwtTokenProvider.parseJwt(header))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Bearer 뒤에 토큰이 없으면 예외가 발생한다")
    @Test
    void parseJwtEmpty() {
        assertThatThrownBy(() -> jwtTokenProvider.parseJwt(BEARER))
            .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("Bearer 뒤에 오는 토큰을 파싱할 수 있다")
    @Test
    void parseJwt() {
        String token = "ABC.DEF.GHI";
        String authorizationHeader = BEARER + token;

        assertThat(jwtTokenProvider.parseJwt(authorizationHeader)).isEqualTo(token);
    }
}
