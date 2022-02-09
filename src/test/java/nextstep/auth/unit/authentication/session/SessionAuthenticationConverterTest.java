package nextstep.auth.unit.authentication.session;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.authentication.session.SessionAuthenticationInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SessionAuthenticationConverterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private SessionAuthenticationConverter authenticationConverter;

    @BeforeEach
    void setUp() {
        authenticationConverter = new SessionAuthenticationConverter();
    }

    @Test
    void convert() {
        HttpServletRequest request = createMockRequest();

        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, String> params = new HashMap<>();
        params.put(SessionAuthenticationInterceptor.USERNAME_FIELD, EMAIL);
        params.put(SessionAuthenticationInterceptor.PASSWORD_FIELD, PASSWORD);
        request.addParameters(params);
        return request;
    }
}