package nextstep.subway.unit.auth.converter;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.AuthConfig;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.token.TokenRequest;

class TokenAuthenticationConverterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private TokenAuthenticationConverter tokenAuthenticationConverter;

    @BeforeEach
    void setUp() {
        this.tokenAuthenticationConverter = new TokenAuthenticationConverter();
    }

    private MockHttpServletRequest createMockRequest() throws JsonProcessingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    @Test
    void convert() throws IOException {
        // when
        AuthenticationToken token = tokenAuthenticationConverter.convert(createMockRequest());

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }
}
