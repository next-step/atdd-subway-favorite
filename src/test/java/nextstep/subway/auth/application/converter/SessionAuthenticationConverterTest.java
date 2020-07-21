package nextstep.subway.auth.application.converter;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("세션 기반 인증의 컨버터 테스트")
class SessionAuthenticationConverterTest {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String NAME = "hyeyoom";
    private static final String PASS = "1234abcd";

    private MockHttpServletRequest request;
    private SessionAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, NAME);
        request.addParameter(PASSWORD_FIELD, PASS);

        converter = new SessionAuthenticationConverter();
    }

    @DisplayName("컨버터 테스트")
    @Test
    void convert() {

        // when
        final AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(NAME);
        assertThat(token.getCredentials()).isEqualTo(PASS);

    }
}