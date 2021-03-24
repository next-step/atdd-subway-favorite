package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 30;

    private CustomUserDetailsService customUserDetailsService;

    private AuthenticationConverter authenticationConverter;

    private SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        this.authenticationConverter = mock(SessionAuthenticationConverter.class);
        this.customUserDetailsService = mock(CustomUserDetailsService.class);
        this.interceptor = new SessionAuthenticationInterceptor(this.customUserDetailsService, this.authenticationConverter);
    }

    @Test
    void preHandle() throws IOException {
        //given
        MockHttpServletRequest httpServletRequest = createMockRequest();
        MockHttpServletResponse httpServletResponse = createMockResponse();
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(createMockLoginMember());
        when(authenticationConverter.convert(httpServletRequest)).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));

        //when
        interceptor.preHandle(httpServletRequest, httpServletResponse, new Object());

        //then
        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    private MockHttpServletResponse createMockResponse() {
        return new MockHttpServletResponse();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private LoginMember createMockLoginMember() {
        return new LoginMember(1L, EMAIL, PASSWORD, AGE);
    }
}
