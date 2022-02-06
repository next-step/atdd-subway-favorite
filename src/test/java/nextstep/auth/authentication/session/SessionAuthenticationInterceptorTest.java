package nextstep.auth.authentication.session;

import nextstep.auth.exception.InvalidPasswordException;
import nextstep.auth.service.UserDetailService;
import nextstep.auth.ui.authentication.AuthenticationInterceptor;
import nextstep.auth.ui.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.ui.authentication.session.SessionAuthenticationInterceptor;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static nextstep.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionAuthenticationInterceptorTest {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String ANOTHER_PASSWORD = "another_password";
    private static final int AGE = 20;

    @Mock
    UserDetailService userDetailsService;

    AuthenticationInterceptor interceptor;
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new SessionAuthenticationInterceptor(new SessionAuthenticationConverter(), userDetailsService);
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        request.addParameter(USERNAME_FIELD, EMAIL);
        request.addParameter(PASSWORD_FIELD, PASSWORD);
    }

    @Test
    void preHandle() throws Exception {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY)).isNotNull();
    }

    @Test
    void invalidPassword() {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, ANOTHER_PASSWORD, AGE));

        // when, then
        assertThatThrownBy(() ->
                interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(InvalidPasswordException.class);
    }

}
