package nextstep.subway.auth.ui.interceptor.convert;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import nextstep.subway.auth.domain.AuthenticationToken;

public class SessionAuthenticationConverterTest {

    private static final String USERNAME = "honux77";
    private static final String PASSWORD = "jujitsu";
    private SessionAuthenticationConverter converter;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        converter = new SessionAuthenticationConverter();
    }

    @DisplayName("토큰을 생성할 수 있다.")
    @Test
    void 토큰을_생성한다() {
        // given
        request.addParameter("username", USERNAME);
        request.addParameter("password", PASSWORD);

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(USERNAME);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("사용자 이름이 없을 경우 오류를 반환한다.")
    @Test
    void 사용자_이름이_존재하지_않으면_오류를_반환한다() {
        request.addParameter("username", PASSWORD);
        assertThatThrownBy(
            () -> converter.convert(request)
        ).isInstanceOf(RuntimeException.class).hasMessage("no principal or credentials found.");
    }

    @DisplayName("비밀번호가 없을 경우 오류를 반환한다.")
    @Test
    void 비밀번호가_존재하지_않으면_오류를_반환한다() {
        request.addParameter("password", PASSWORD);
        assertThatThrownBy(
            () -> converter.convert(request)
        ).isInstanceOf(RuntimeException.class).hasMessage("no principal or credentials found.");
    }
}
