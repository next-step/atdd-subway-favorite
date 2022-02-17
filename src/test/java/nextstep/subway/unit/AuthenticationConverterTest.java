package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SessionAuthTokenConverter;
import nextstep.auth.authentication.TokenAuthTokenConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.unit.MockHttpRequestFixtures.createSessionMockRequest;
import static nextstep.subway.unit.MockHttpRequestFixtures.createTokenMockRequest;
import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationConverterTest {
    private AuthenticationConverter authenticationConverter;

    @AfterEach
    void clear() {
        authenticationConverter = null;
    }

    @Test
    void sessionConvert() {
        //given
        authenticationConverter = new SessionAuthTokenConverter();
        String email = "my@email.com";
        String password = "password";
        HttpServletRequest request = createSessionMockRequest(email, password);

        //when
        AuthenticationToken token = authenticationConverter.convert(request);

        //then
        assertThat(token.getPrincipal()).isEqualTo(email);
        assertThat(token.getCredentials()).isEqualTo(password);
    }

    @Test
    void tokenConvert() {
        authenticationConverter = new TokenAuthTokenConverter(new ObjectMapper());
        String email = "my@email.com";
        String password = "password";
        HttpServletRequest request = createTokenMockRequest(email, password);

        //when
        AuthenticationToken token = authenticationConverter.convert(request);

        //then
        assertThat(token.getPrincipal()).isEqualTo(email);
        assertThat(token.getCredentials()).isEqualTo(password);
    }
}
