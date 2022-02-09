package nextstep.auth.unit.authentication.session;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.unit.authentication.MockAuthenticationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

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
        HttpServletRequest request = MockAuthenticationRequest.createSessionRequest(EMAIL, PASSWORD);

        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}