package nextstep.subway.unit.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenAuthenticationConverterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Test
    void convert() throws IOException {
        AuthenticationConverter converter = new TokenAuthenticationConverter();
        MockHttpServletRequest request = createMockRequest();
        AuthenticationToken authenticationToken = converter.convert(request);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setMethod(HttpMethod.POST.name());
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
