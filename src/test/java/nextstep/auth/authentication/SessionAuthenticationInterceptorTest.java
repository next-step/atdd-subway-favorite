package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class SessionAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private AuthenticationConverter authenticationConverter;
    private SessionAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() throws IOException {
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, authenticationConverter);
    }

    @DisplayName("로그인 정보를 세션에 저장하고 응답한다")
    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest 요청 = mock_요청생성();
        MockHttpServletResponse 응답 = mock_응답생성();
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(loginMember);
        when(authenticationConverter.convert(any()))
                .thenReturn(new AuthenticationToken(EMAIL, PASSWORD));

        // when
        boolean result = interceptor.preHandle(요청, 응답, new Object());

        // then
        assertThat(result).isFalse();
        assertThat(응답.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("AuthenticationToken을 이용해 Authentication을 생성한다")
    @Test
    void authenticate() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(loginMember);

        // when
        Authentication authenticate = interceptor.authenticate(authenticationToken);

        // then
        assertThat(authenticate.getPrincipal()).isInstanceOf(LoginMember.class);
        assertThat(authenticate.getPrincipal()).isNotNull();
    }

    @DisplayName("회원 정보가 없으면 Authentication 생성을 실패한다")
    @Test
    void authenticate_fail() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        LoginMember loginMember = new LoginMember(1L, "mail", "notMatch", 10);
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(loginMember);

        // then
        assertThatThrownBy(() -> interceptor.authenticate(authenticationToken))
                .isInstanceOf(AuthenticationException.class);
    }

    private MockHttpServletRequest mock_요청생성() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", EMAIL);
        request.addParameter("password", PASSWORD);

        return request;
    }

    private MockHttpServletResponse mock_응답생성() {
        return new MockHttpServletResponse();
    }

}