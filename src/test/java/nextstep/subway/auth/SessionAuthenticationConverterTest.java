package nextstep.subway.auth;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionAuthenticationConverterTest {

    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private final AuthenticationConverter converter = new SessionAuthenticationConverter();

    @Test
    void convert() throws IOException {
        // when
        MockHttpServletRequest request = createMockRequest();
        AuthenticationToken authenticationToken = converter.convert(request);

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
