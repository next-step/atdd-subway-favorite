package nextstep.subway.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.NotValidPasswordException;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String NEW_PASSWORD = "new_password";

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private TokenAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        customUserDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider, objectMapper);
    }

    @Test
    void convert() throws IOException {
        // given
        final MockHttpServletRequest mockRequest = createMockRequest();

        // when
        final AuthenticationToken authenticationToken = interceptor.convert(mockRequest);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() {
        // given
        given(customUserDetailsService.loadUserByUsername(any())).willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        // when
        final Authentication authenticate = interceptor.authenticate(new AuthenticationToken(EMAIL, PASSWORD));

        // then
        assertThat(authenticate.getPrincipal()).isNotNull();
        assertThat(authenticate.getPrincipal()).isInstanceOf(LoginMember.class);
    }

    @Test
    void authenticateInvalidPassword() {
        // given
        given(customUserDetailsService.loadUserByUsername(any())).willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        // when, then
        assertThatThrownBy(
            () -> interceptor.authenticate(new AuthenticationToken(EMAIL, NEW_PASSWORD)))
            .isInstanceOf(NotValidPasswordException.class);
    }

    @Test
    void preHandle() throws IOException {
        // given
        final MockHttpServletRequest request = createMockRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        given(customUserDetailsService.loadUserByUsername(any())).willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        given(jwtTokenProvider.createToken(any())).willReturn(JWT_TOKEN);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
