package nextstep.auth.unit.authentication.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class TokenAuthenticationConverterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private TokenAuthenticationConverter authenticationConverter;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationConverter = new TokenAuthenticationConverter(objectMapper);
    }

    @Test
    void convert() throws IOException {
        HttpServletRequest request = createMockRequest();

        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}