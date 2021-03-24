package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.UserDetails;
import nextstep.subway.auth.ui.AuthenticationInterceptor;
import nextstep.subway.auth.ui.converter.AuthenticationConverter;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AuthSteps {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 30;

    public static MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    public static MockHttpServletRequest createMockSessionRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);
        return request;
    }

    public static void checkResponse(AuthenticationConverter converter, MockHttpServletRequest request) throws IOException {
        AuthenticationToken authenticationToken = converter.converter(request);

        assertEquals(authenticationToken.getPrincipal(), EMAIL);
        assertEquals(authenticationToken.getCredentials(), PASSWORD);
    }

    public static void authenticate(UserDetails userDetails, AuthenticationInterceptor interceptor) {
        when(userDetails).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        Authentication authentication = interceptor.authenticate(authenticationToken);

        assertThat(authentication.getPrincipal()).isNotNull();
    }
}
