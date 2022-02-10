package nextstep.subway.unit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.unit.TokenAuthenticationUnitTestHelper.EMAIL;
import static nextstep.subway.unit.TokenAuthenticationUnitTestHelper.PASSWORD;
import static nextstep.subway.unit.TokenAuthenticationUnitTestHelper.createMockTokenRequest;
import static nextstep.subway.unit.TokenAuthenticationUnitTestHelper.getAccessToken;
import static nextstep.subway.unit.TokenAuthenticationUnitTestHelper.getPayload;
import static nextstep.subway.unit.TokenAuthenticationUnitTestHelper.getUserDetails;
import static nextstep.subway.unit.TokenGenerator.getToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Spy
    private ObjectMapper objectMapper;
    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    /**
     * Given 토큰 생성 요청 객체를 만들고
     * When Request Body 에 담아서 변환을 요청하면
     * Then 생성된 인증 토큰을 받는다
     */
    @Test
    void convert() throws IOException {
        인증_토큰_생성();
    }

    /**
     * Given 회원을 생성하고
     * When 토큰 생성 요청 객체를 만들고, Request Body 에 담아서 변환을 요청하면
     * Then 생성된 인증 토큰을 받는다
     * When 인증 토큰을 Request Body 에 담아서 인증 객체 생성을 요청하면
     * Then 생성된 인증 생성 객체를 받는다
     */
    @Test
    void authenticate() throws IOException {
        LoginMember userDetails = getUserDetails();
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(인증_토큰_생성());
        assertThat(authentication.getPrincipal()).isNotNull();
        assertThat(authentication.getPrincipal()).isInstanceOf(LoginMember.class);
    }

    /**
     * Given 회원을 생성하고
     * And 인증 토큰을 생성하고
     * When 인증 토큰을 Request Body 에 담아서 preHandle 을 호출하면
     * Then Response Body 에 생성된 Access Token 이 들어있다.
     */
    @Test
    void preHandle() throws IOException {
        LoginMember userDetails = getUserDetails();
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(jwtTokenProvider.createToken(getPayload(userDetails))).thenReturn(getToken());

        MockHttpServletRequest request = createMockTokenRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        tokenAuthenticationInterceptor.preHandle(request, response, null);

        assertThat(getAccessToken(response)).isEqualTo(getToken());
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
