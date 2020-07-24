package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.interceptor.convert.AuthenticationConverter;
import nextstep.subway.member.application.UserDetailsService;
import nextstep.subway.member.domain.LoginMember;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private static final long ID = 1L;

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private AuthenticationConverter converter;

    private SessionAuthenticationInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, converter);
    }

    @DisplayName("세션 인터셉터 테스트")
    @Test
    void preHandle() throws IOException {
        // given
        when(converter.convert(request)).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);

        // when
        boolean result = interceptor.preHandle(request, response, new Object());
        LoginMember loginMember = getLoginMember();

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result).isFalse(),
                () -> assertThat(loginMember.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(loginMember.getPassword()).isEqualTo(PASSWORD)
        );
    }

    private LoginMember getLoginMember() {
        Authentication authentication = getAuthentication();
        return (LoginMember) authentication.getPrincipal();
    }

    private Authentication getAuthentication() {
        SecurityContext context = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        return context.getAuthentication();
    }

}
