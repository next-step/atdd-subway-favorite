package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.utils.MockRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationConverterTest {

    @DisplayName("이메일/비밀번호가 담긴 토큰을 반환한다.")
    @Test
    void testConvertTokenRequest() throws IOException {
        // given
        TokenAuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter(new ObjectMapper());
        MockHttpServletRequest request = createMockTokenRequest();

        // when
        AuthenticationToken token = tokenAuthenticationConverter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("세션으로 이메일/비밀번호가 담긴 토큰을 반환한다.")
    @Test
    void testConvertSessionRequest() {
        // given
        SessionAuthenticationConverter sessionAuthenticationConverter = new SessionAuthenticationConverter();
        MockHttpServletRequest request = createMockSessionRequest();

        // when
        AuthenticationToken token = sessionAuthenticationConverter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }
}
