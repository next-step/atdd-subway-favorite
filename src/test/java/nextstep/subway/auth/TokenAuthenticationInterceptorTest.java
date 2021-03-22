package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void convert() throws IOException {
        // given
        final CustomUserDetailsService customUserDetailsService = mock(CustomUserDetailsService.class);
        final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        final TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider,
            objectMapper);
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
        final CustomUserDetailsService customUserDetailsService = mock(CustomUserDetailsService.class);
        final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        final TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider,
            objectMapper);

        // when
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        final Authentication authenticate = interceptor.authenticate(new AuthenticationToken(EMAIL, PASSWORD));

        // then
        assertThat(authenticate.getPrincipal()).isNotNull();
        assertThat(authenticate.getPrincipal()).isInstanceOf(LoginMember.class);
    }

    @Test
    void preHandle() throws IOException {
        // given
        final CustomUserDetailsService customUserDetailsService = mock(CustomUserDetailsService.class);
        final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        final TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider,
            objectMapper);
        final MockHttpServletRequest request = createMockRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        when(jwtTokenProvider.createToken(any())).thenReturn(JWT_TOKEN);
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
