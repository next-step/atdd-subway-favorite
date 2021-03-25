package nextstep.subway.auth.ui.session;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static nextstep.subway.auth.AuthRequestSteps.createMockSessionRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SessionAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Mock
    private CustomUserDetailsService userDetailsService;

    private SessionAuthenticationConverter sessionAuthenticationConverter;
    private SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        sessionAuthenticationConverter = new SessionAuthenticationConverter();
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, sessionAuthenticationConverter);
    }

    @Test
    void authenticate() {
        // given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 20);
        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(loginMember);

        // when
        MockHttpServletRequest request = createMockSessionRequest();
        AuthenticationToken authenticationToken = sessionAuthenticationConverter.convert(request);
        Authentication authentication = interceptor.authenticate(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @Test
    void preHandle() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 20);
        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(loginMember);

        // when
        MockHttpServletRequest request = createMockSessionRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
