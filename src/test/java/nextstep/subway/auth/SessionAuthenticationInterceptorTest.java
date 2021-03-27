package nextstep.subway.auth;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.converter.AuthenticationConverter;
import nextstep.subway.auth.domain.converter.SessionAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor.PASSWORD_FIELD;
import static nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor.USERNAME_FIELD;
import static org.assertj.core.api.Assertions.assertThat;

class SessionAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Test
    void convert() {
        // given
        AuthenticationConverter authenticationConverter = new SessionAuthenticationConverter();
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);
        return request;
    }
}
