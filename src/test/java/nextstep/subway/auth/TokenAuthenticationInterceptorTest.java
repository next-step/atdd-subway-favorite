package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static nextstep.subway.auth.AuthSteps.*;

class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private MockHttpServletRequest request;
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;
    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException{
        request = createMockRequest(EMAIL, PASSWORD);
        customUserDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        objectMapper = new ObjectMapper();
        tokenAuthenticationInterceptor =
                new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider, objectMapper);
    }

    @DisplayName("request를 auth token으로")
    @Test
    void convert() throws IOException {
        //when
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(request);

        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("auth token을 auth 객체로")
    @Test
    void authenticate() {
        //given
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 30));

        //when
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        //then
        assertThat(((LoginMember) authentication.getPrincipal()).getEmail()).isEqualTo(EMAIL);

    }

    @DisplayName("interceptor에서 preHandle 처리")
    @Test
    void preHandle() throws IOException {
        //given
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        MockHttpServletRequest request = AuthSteps.createMockRequest(EMAIL, PASSWORD);
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        tokenAuthenticationInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }
}
