package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import static org.mockito.Mockito.when;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Test
    void convert() throws IOException {
        //when
        AuthenticationToken authenticationToken = interceptor.convert(createMockRequest());

        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() {
        //given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 20);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        //when
        Authentication authenticate = interceptor.authenticate(authenticationToken);

        //then
        assertThat(authenticate.getPrincipal()).isEqualTo(loginMember);
    }

    @Test
    void preHandle() throws IOException {
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private TokenAuthenticationInterceptor interceptor;
    @BeforeEach
    void setUp() {
        userDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);
    }

}
