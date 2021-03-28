package nextstep.subway.auth.ui.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


class AuthenticationConverterTest {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("요청객체에서 세션정보를 읽어 인증토큰으로 변환")
    @Test
    void convertSession() throws IOException {
        // given
        AuthenticationConverter authenticationConverter = new SessionAuthenticationConverter();
        MockHttpServletRequest request = createMockRequest();
        setSession(request);

        // when
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("요청객체에서 토큰정보를 읽어 인증토큰으로 변환")
    @Test
    void convertToken() throws IOException {
        // given
        AuthenticationConverter authenticationConverter = new TokenAuthenticationConverter();
        MockHttpServletRequest request = createMockRequest();
        setToken(request);

        // when
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        return request;
    }

    private void setSession(MockHttpServletRequest request){
        request.setParameter(USERNAME_FIELD, EMAIL);
        request.setParameter(PASSWORD_FIELD, PASSWORD);
    }

    private void setToken(MockHttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
    }

}