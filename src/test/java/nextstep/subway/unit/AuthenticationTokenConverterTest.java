package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.token.AuthenticationTokenConverter;
import nextstep.auth.token.TokenRequest;
import nextstep.subway.support.MockRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationTokenConverterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("HttpRequest로 AuthenticationToken 생성")
    void convert() throws IOException {
        AuthenticationTokenConverter converter = new AuthenticationTokenConverter(new ObjectMapper());
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        MockHttpServletRequest mockHttpServletRequest = MockRequest.createTokenRequest(tokenRequest);

        AuthenticationToken token = converter.convert(mockHttpServletRequest);

        AuthenticationToken_생성_검증(token);
    }

    private void AuthenticationToken_생성_검증(AuthenticationToken token) {
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }
}
