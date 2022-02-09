package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("세션 기반 AuthenticationToken ")
class TokenAuthenticationConverterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private TokenAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TokenAuthenticationConverter();
    }

    @DisplayName("")
    @Test
    void convert() throws JsonProcessingException {
        // when
        AuthenticationToken token = converter.convert(mock_요청생성());

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest mock_요청생성() throws JsonProcessingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());

        return request;
    }

}