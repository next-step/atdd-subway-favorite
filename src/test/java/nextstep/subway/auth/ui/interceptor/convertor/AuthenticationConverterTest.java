package nextstep.subway.auth.ui.interceptor.convertor;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.interceptor.converter.AuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.converter.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.converter.TokenAuthenticationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationConverterTest {
    private static final String EMAIL = "test@test.com";
    private static final String REGEX = ":";
    private static final String PASSWORD = "test";

    private AuthenticationConverter sessionConverter;
    private AuthenticationConverter tokenConverter;

    private MockHttpServletRequest sessionRequest;
    private MockHttpServletRequest tokenRequest;

    @BeforeEach
    void setUp() {
        sessionRequest = new MockHttpServletRequest();
        sessionConverter = new SessionAuthenticationConverter();
        tokenConverter = new TokenAuthenticationConverter();

        setSessionRequestHeader();
        setBasicAuthHeader();
    }

    void setSessionRequestHeader() {
        Map<String, String> params = new HashMap<>();
        params.put("username", EMAIL);
        params.put("password", PASSWORD);

        sessionRequest.addParameters(params);
    }

    private void setBasicAuthHeader() {
        byte[] targetBytes = (EMAIL + REGEX + PASSWORD).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        tokenRequest.addHeader("Authorization", "Basic " + credentials);
    }

    @DisplayName("Session, token Converter 생성")
    @Test
    void createConverter() {
        assertThat(sessionConverter).isNotNull();
        assertThat(tokenConverter).isNotNull();
    }

    @DisplayName("Session -> AuthenticationToken 변환")
    @Test
    void convertSessionToAuthenticationToken() {
        // when
        AuthenticationToken token = sessionConverter.convert(sessionRequest);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("Token -> AuthenticationToken 변환")
    @Test
    void convertTokenToAuthenticationToken() {
        // when
        AuthenticationToken token = tokenConverter.convert(sessionRequest);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }
}
