package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    public static final String REGEX = ":";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private TokenAuthenticationInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private UserDetails userDetails = new UserDetails() {
        @Override
        public Object getPrincipal() {
            return EMAIL;
        }

        @Override
        public Object getCredentials() {
            return PASSWORD;
        }

        @Override
        public boolean checkCredentials(Object credentials) {
            return true;
        }
    };

    @BeforeEach
    void setUp() {
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);

        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle() throws IOException {
        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        TokenResponse tokenResponse = new ObjectMapper().readValue(response.getContentAsString(), TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isEqualTo(JWT_TOKEN);
    }

    @Test
    void convert() {
        // when
        AuthenticationToken authenticationToken = interceptor.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void attemptAuthentication() {
        // when
        Authentication authentication = interceptor.attemptAuthentication(request, response);

        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getPrincipal()).isEqualTo(EMAIL);
        assertThat(((UserDetails) authentication.getPrincipal()).getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() {
        byte[] targetBytes = (EMAIL + REGEX + PASSWORD).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic " + credentials);
        return request;
    }
}