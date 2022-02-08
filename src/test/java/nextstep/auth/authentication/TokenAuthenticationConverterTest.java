package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class TokenAuthenticationConverterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("application/json 타입의 요청을 AuthenticaionToken으로 변환한다")
    @Test
    void convert() throws JsonProcessingException {
        // given
        TokenAuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());

        // when
        AuthenticationToken authentication = tokenAuthenticationConverter.convert(request);

        // then
        assertThat(authentication).isEqualTo(new AuthenticationToken(EMAIL, PASSWORD));
    }
}