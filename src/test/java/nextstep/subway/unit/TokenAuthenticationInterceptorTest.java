package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.interceptors.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.userdetails.User;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @InjectMocks
    private TokenAuthenticationInterceptor target;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserDetailsService detailsService;

    @Spy
    private ObjectMapper objectMapper;

    @Test
    void crateLoginRequest() throws IOException {
        final AuthenticateRequest loginRequest = target.createLoginRequest(createMockRequest());
        assertThat(loginRequest).isNotNull();
    }

    @Test
    void preHandle() throws Exception {
        final UserDetails userDetails = new User(EMAIL, PASSWORD, List.of());

        doReturn(userDetails)
                .when(detailsService)
                        .loadUserByUsername(EMAIL);

        doReturn(JWT_TOKEN)
                .when(tokenProvider)
                .createToken(userDetails.getEmail(), userDetails.getAuthorities());

        final boolean result = target.preHandle(createMockRequest(), createMockResponse(), null);

        assertThat(result).isFalse();
    }

    @Test
    void preHandle_사용자가없음() throws Exception {
        doReturn(null)
                .when(detailsService)
                        .loadUserByUsername(EMAIL);

        assertThrows(
                AuthenticationException.class,
                () -> target.preHandle(createMockRequest(), createMockResponse(), null));
    }

    @Test
    void afterAuthenticate() throws IOException {
        final UserDetails userDetails = new User(EMAIL, PASSWORD, List.of());

        doReturn(JWT_TOKEN)
                .when(tokenProvider)
                .createToken(userDetails.getEmail(), userDetails.getAuthorities());

        target.afterAuthenticate(createMockResponse(), userDetails);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        AuthenticateRequest authenticateRequest = new AuthenticateRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(authenticateRequest).getBytes());
        return request;
    }

    private MockHttpServletResponse createMockResponse() {
        return new MockHttpServletResponse();
    }

}
