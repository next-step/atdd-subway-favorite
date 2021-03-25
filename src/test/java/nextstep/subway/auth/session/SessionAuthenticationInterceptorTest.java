package nextstep.subway.auth.session;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.common.AuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.member.domain.LoginMember;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.subway.auth.session.SessionFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionAuthenticationInterceptorTest {
    private SessionAuthenticationInterceptor interceptor;
    private UserDetailService userDetailsService;
    private AuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(CustomUserDetailsService.class);
        converter = new SessionAuthenticationConverter();
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, converter);
    }

    @Test
    void authenticate() {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        // when
        Authentication authenticate = interceptor.authenticate(authenticationToken);

        // then
        assertThat(authenticate).isNotNull();
    }

    @Test
    void preHandle() throws IOException {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY)).isNotNull();
    }
}
