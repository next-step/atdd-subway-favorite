package nextstep.subway.auth.ui.interceptor.convert;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionAuthenticationConverterTest {

    private SessionAuthenticationConverter converter;
    private MockHttpServletRequest request;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        converter = new SessionAuthenticationConverter();
    }

    @DisplayName("token 생성 테스트")
    @Test
    void AuthConvert() {
        // given
        request.addParameter("username", USERNAME);
        request.addParameter("password", PASSWORD);

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(USERNAME);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("username이 없을시 에러 발생")
    @Test
    void withOutUsername() {
        request.addParameter("password", PASSWORD);

        assertThatThrownBy(() -> converter.convert(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("password가 없을시 에러 발생")
    @Test
    void withOutPassword() {
        request.addParameter("username", USERNAME);

        assertThatThrownBy(() -> converter.convert(request))
                .isInstanceOf(RuntimeException.class);
    }

}
