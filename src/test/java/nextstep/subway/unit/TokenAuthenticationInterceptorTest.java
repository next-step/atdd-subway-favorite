package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.authentication.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.utils.MockRequest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest {
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private UserDetailService userDetailService;
    private ObjectMapper objectMapper;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationConverter authenticationConverter;
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @BeforeEach
    void setUp() {
        userDetailService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        objectMapper = new ObjectMapper();
        authenticationConverter = new TokenAuthenticationConverter(objectMapper);
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(authenticationConverter, userDetailService, jwtTokenProvider, objectMapper);
    }

    @DisplayName("이메일/비밀번호가 담긴 토큰을 반환한다.")
    @Test
    void convert() throws IOException {
        // given
        MockHttpServletRequest request = createMockTokenRequest();

        // when
        AuthenticationToken token = tokenAuthenticationInterceptor.convert(request);

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
        when(userDetailService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);

        // when
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(token);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(loginMember);
    }

    @DisplayName("이메일/비밀번호로 인증토큰을 얻는다.")
    @Test
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest request = createMockTokenRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LoginMember loginMember = LoginMember.of(new Member(EMAIL, PASSWORD, 10));
        when(userDetailService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(any())).thenReturn(JWT_TOKEN);

        // when
        tokenAuthenticationInterceptor.preHandle(request, response, null);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }


}
