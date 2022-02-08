package nextstep.subway.unit.service;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.unit.TokenAuthenticationUnitTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @Test
    void convert() throws IOException {
        인증_토큰_생성();
    }

    /**
     * When 토큰 생성 요청 객체를 만들고, Request Body 에 담아서 변환을 요청하면
     * Then 생성된 인증 토큰을 받는다
     * When 인증 토큰을 Request Body 에 담아서 인증 객체 생성을 요청하면
     * Then 생성된 인증 생성 객체를 받는다
     */
    @Test
    void authenticate() throws IOException {
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(인증_토큰_생성());
        assertThat(authentication.getPrincipal()).isNotNull();
        assertThat(authentication.getPrincipal()).isInstanceOf(LoginMember.class);
    }

    @Test
    void preHandle() throws IOException {
    }

    /**
     * Given 토큰 생성 요청 객체를 만들고
     * When Request Body 에 담아서 변환을 요청하면
     * Then 생성된 인증 토큰을 받는다
     */
    private AuthenticationToken 인증_토큰_생성() throws IOException {
        MockHttpServletRequest request = createMockTokenRequest();
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(request);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
        return authenticationToken;
    }
}
