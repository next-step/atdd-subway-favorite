package nextstep.subway.auth;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.converter.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionAuthenticationInterceptorTest {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 30;

    private UserDetailsService userDetailsService;

    SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(UserDetailsService.class);
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, new SessionAuthenticationConverter());
    }


    @Test
    void authenticate() {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        Authentication authentication = interceptor.authenticate(authenticationToken);

        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @Test
    void preHandle() throws IOException {

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        MockHttpServletRequest request = createMockSessionRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    public static MockHttpServletRequest createMockSessionRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);
        return request;
    }
}
