package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.interceptor.authentication.converter.AuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.authentication.interceptor.SessionAuthenticationInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password1234";

    private SessionAuthenticationInterceptor interceptor;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationConverter authenticationConverter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, authenticationConverter);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @DisplayName("패스워드가 일치하지 않으면 RuntimeException")
    @Test
    void authenticate_notEqualPassword() {
        // given
        when(authenticationConverter.convert(request)).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, "notequalpassword", 10));

        // when
        assertThatThrownBy(() -> interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Session에 유저 정보를 저장한다")
    @Test
    void response() throws IOException {
        // given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(authenticationConverter.convert(request)).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication = securityContext.getAuthentication();
        LoginMember member = (LoginMember) authentication.getPrincipal();

        assertThat(member.getEmail()).isEqualTo(EMAIL);
        assertThat(member.getPassword()).isEqualTo(PASSWORD);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
