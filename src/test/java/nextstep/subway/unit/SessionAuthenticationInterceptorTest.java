package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthenticationTokenConverter;
import nextstep.auth.authentication.SessionAuthenticationTokenConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class SessionAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @Test
    void newConvert() throws IOException {
        // given
        AuthenticationTokenConverter converter = new SessionAuthenticationTokenConverter();
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken authenticationToken = converter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", EMAIL);
        request.addParameter("password", PASSWORD);
        return request;
    }

}
