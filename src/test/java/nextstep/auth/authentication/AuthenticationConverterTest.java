package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.domain.AuthenticationToken;
import nextstep.auth.dto.TokenRequest;
import nextstep.auth.ui.authentication.AuthenticationConverter;
import nextstep.auth.ui.authentication.SessionAuthenticationConverter;
import nextstep.auth.ui.authentication.TokenAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationConverterTest {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private ObjectMapper objectMapper = new ObjectMapper();
    private AuthenticationConverter converter;

    @Test
    void convertWithSession() throws IOException{
        // given
        MockHttpServletRequest request = createRequestForSession();
        converter = new SessionAuthenticationConverter();

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void convertWithToken() throws IOException{
        // given
        MockHttpServletRequest request = createRequestForToken();
        converter = new TokenAuthenticationConverter(objectMapper);

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createRequestForSession() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);
        return request;
    }

    private MockHttpServletRequest createRequestForToken() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(objectMapper.writeValueAsString(new TokenRequest(EMAIL, PASSWORD)).getBytes());
        return request;
    }
}
