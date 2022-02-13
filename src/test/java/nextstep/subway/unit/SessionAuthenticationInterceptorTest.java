package nextstep.subway.unit;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.interceptor.SessionAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.utils.MockRequest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionAuthenticationInterceptorTest {
    private UserDetailService userDetailsService;
    private AuthenticationConverter authenticationConverter;
    private SessionAuthenticationInterceptor sessionAuthenticationInterceptor;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(CustomUserDetailsService.class);
        authenticationConverter = new SessionAuthenticationConverter();
        sessionAuthenticationInterceptor = new SessionAuthenticationInterceptor(authenticationConverter, userDetailsService);
    }

    @DisplayName("이메일/비밀번호가 담긴 토큰을 반환한다.")
    @Test
    void convert() throws IOException {
        // given
        MockHttpServletRequest request = createMockSessionRequest();

        // when
        AuthenticationToken token = sessionAuthenticationInterceptor.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("회원 인증한다.")
    @Test
    void authenticate() {
        // given
        Member member = new Member(EMAIL, PASSWORD, 10);
        LoginMember loginMember = LoginMember.of(member);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);

        // when
        Authentication authentication = sessionAuthenticationInterceptor.authenticate(token);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(loginMember);
    }

    @DisplayName("이메일/비밀번호로 인증한다")
    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest request = createMockSessionRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LoginMember loginMember = LoginMember.of(new Member(EMAIL, PASSWORD, 10));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);

        // when
        sessionAuthenticationInterceptor.preHandle(request, response, null);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


}
