package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenAuthenticationConverterTest {
    private static final String EMAIL = "gpwls89";
    private static final String PASSWORD = "1234";

    private TokenAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TokenAuthenticationConverter(new ObjectMapper());
    }

    @Test
    void convert() throws IOException {
        //when
        AuthenticationToken authenticationToken = converter.convert(createMockRequest());

        //then
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
