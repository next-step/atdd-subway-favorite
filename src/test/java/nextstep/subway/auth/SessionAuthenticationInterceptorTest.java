package nextstep.subway.auth;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.dto.UserDetails;
import nextstep.subway.auth.ui.converter.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.auth.AuthSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionAuthenticationInterceptorTest {

    private UserDetailsService userDetailsService;

    SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(UserDetailsService.class);
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, new SessionAuthenticationConverter());
    }

    @Test
    void authenticate() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(EMAIL);
        AuthSteps.authenticate(userDetails, interceptor);
    }

    @Test
    void preHandle() throws IOException {

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        MockHttpServletRequest request = createMockSessionRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
