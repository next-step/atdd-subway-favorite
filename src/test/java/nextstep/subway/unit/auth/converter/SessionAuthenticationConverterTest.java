package nextstep.subway.unit.auth.converter;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;

class SessionAuthenticationConverterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private SessionAuthenticationConverter sessionAuthenticationConverter;

    @BeforeEach
    void setUp() {
        this.sessionAuthenticationConverter = new SessionAuthenticationConverter();
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", EMAIL);
        request.addParameter("password", PASSWORD);
        return request;
    }

    @Test
    void matchesAuthenticationType() {
        assertThat(sessionAuthenticationConverter.matchRequestUri("/login/session")).isTrue();
    }

    @Test
    void convert() {
        // when
        AuthenticationToken token = sessionAuthenticationConverter.convert(createMockRequest());

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }
}
