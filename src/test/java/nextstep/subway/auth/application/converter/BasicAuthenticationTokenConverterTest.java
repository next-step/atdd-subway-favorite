package nextstep.subway.auth.application.converter;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class BasicAuthenticationTokenConverterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String REGEX = ":";

    @Test
    void convert() {
        BasicAuthenticationTokenConverter converter = new BasicAuthenticationTokenConverter();
        MockHttpServletRequest request = createMockRequest();

        AuthenticationToken authenticationToken = converter.convert(request);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() {
        byte[] targetBytes = (EMAIL + REGEX + PASSWORD).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic " + credentials);
        return request;
    }
}