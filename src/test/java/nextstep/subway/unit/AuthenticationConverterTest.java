package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthenticationConverterTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private MockHttpServletRequest createMockSessionRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(USERNAME_FIELD, EMAIL);
        paramMap.put(PASSWORD_FIELD, PASSWORD);
        request.setParameters(paramMap);
        return request;
    }

    private MockHttpServletRequest createMockTokenRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    @DisplayName("세션 기반 인증")
    @Test
    void sessionAuthenticationConvert() throws IOException {
        MockHttpServletRequest request = createMockSessionRequest();
        AuthenticationToken authenticationToken = new SessionAuthenticationConverter().convert(request);
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("토큰 기반 인증")
    @Test
    void tokenAuthenticationConvert() throws IOException {
        MockHttpServletRequest request = createMockTokenRequest();
        AuthenticationToken authenticationToken = new TokenAuthenticationConverter(new ObjectMapper()).convert(request);
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}
