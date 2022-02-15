package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.UserDetailService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Test
    void authenticate() {
        // given
        UserDetailService userDetailsService = mock(UserDetailService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        AuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, new ObjectMapper());

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        // when
        Authentication authenticate = interceptor.authenticate(authenticationToken);

        // then
        assertThat(authenticate.getPrincipal()).isNotNull();
    }

    @Test
    void preHandle() throws IOException {
        // given
        UserDetailService userDetailsService = mock(UserDetailService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        AuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, new ObjectMapper());

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    @DisplayName("이메일이 틀릴 경우")
    @Test
    void exception_notExistsEmailAuthenticate() {
        // given
        UserDetailService userDetailsService = mock(UserDetailService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        AuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, new ObjectMapper());

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(null);

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        // then
        assertThatThrownBy(() -> interceptor.authenticate(authenticationToken))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
    }

    @DisplayName("비밀번호 틀릴 경우")
    @Test
    void exception_wrongPasswordAuthenticate() {
        // given
        UserDetailService userDetailsService = mock(UserDetailService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        AuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, new ObjectMapper());

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, "111");

        // then
        assertThatThrownBy(() -> interceptor.authenticate(authenticationToken))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("비밀번호가 틀렸습니다.");
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
