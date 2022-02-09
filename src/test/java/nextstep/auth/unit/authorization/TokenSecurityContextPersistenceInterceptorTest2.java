package nextstep.auth.unit.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.DefaultUserDetails;
import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authorization.AbstractSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor2;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class TokenSecurityContextPersistenceInterceptorTest2 {
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private AbstractSecurityContextPersistenceInterceptor interceptor;

    @BeforeEach
    void setUp() throws IOException {
        UserDetails userDetails = new DefaultUserDetails(EMAIL, PASSWORD);
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(userDetails);

        JwtTokenProvider tokenProvider = mock(JwtTokenProvider.class);
        Mockito.when(tokenProvider.validateToken(JWT_TOKEN)).thenReturn(true);
        Mockito.when(tokenProvider.getPayload(JWT_TOKEN)).thenReturn(payload);

        interceptor = new TokenSecurityContextPersistenceInterceptor2(tokenProvider);
    }

    @Test
    void preHandle() throws Exception {
        MockHttpServletRequest request = MockAuthorizationRequest.createTokenRequest(JWT_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
    }

    @Test
    void afterCompletion() throws Exception {
        MockHttpServletRequest request = MockAuthorizationRequest.createTokenRequest(JWT_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();

        interceptor.afterCompletion(request, response, null, null);
        Authentication updatedAuthentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(updatedAuthentication).isNull();
    }
}
