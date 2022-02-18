package nextstep.auth.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.auth.unit.AuthData.EMAIL;
import static nextstep.auth.unit.AuthData.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

class TokenAuthenticationConverterTest {
    @DisplayName("application/json 타입의 요청을 AuthenticaionToken으로 변환한다")
    @Test
    void convert() throws IOException {
        // given
        TokenAuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter(new ObjectMapper());

        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());

        // when
        AuthenticationToken authentication = tokenAuthenticationConverter.convert(request);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getCredentials()).isEqualTo(PASSWORD);
    }
}