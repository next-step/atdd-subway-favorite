package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.BDDMockito.given;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private TokenAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenAuthenticationInterceptor(
                userDetailsService,
                jwtTokenProvider,
                new ObjectMapper(),
                new TokenAuthenticationConverter(new ObjectMapper())
        );
    }

    @Test
    void authenticate() {
        //given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 20);
        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(loginMember);
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        //when
        Authentication authenticate = interceptor.authenticate(authenticationToken);

        //then
        val principal = (LoginMember) authenticate.getPrincipal();
        assertThat(principal.getEmail()).isEqualTo(EMAIL);
        assertThat(principal.getPassword()).isEqualTo(PASSWORD);
        assertThat(principal.getAge()).isEqualTo(20);
    }

    @Test
    void preHandle() throws IOException {
        //given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 20);
        given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(loginMember);
        given(jwtTokenProvider.createToken(any())).willReturn(JWT_TOKEN);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        interceptor.preHandle(createMockRequest(), response, new Object());

        int status = response.getStatus();
        String contentType = response.getContentType();
        String content = response.getContentAsString();

        //then
        assertThat(status).isEqualTo(HttpStatus.OK.value());
        assertThat(contentType).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(content).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
