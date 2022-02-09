package nextstep.auth.unit.authentication.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.*;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.authentication.token.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.unit.authentication.MockAuthenticationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

class TokenAuthenticationInterceptorTest {
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private TokenAuthenticationInterceptor interceptor;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);

        UserDetails expectedUserDetails = new DefaultUserDetails(EMAIL, PASSWORD);
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(expectedUserDetails);
        Mockito.when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        ProviderManager providerManager = new ProviderManager(Collections.singletonList(new UsernamePasswordAuthenticationProvider(userDetailsService)));
        interceptor = new TokenAuthenticationInterceptor(jwtTokenProvider, objectMapper, new TokenAuthenticationConverter(objectMapper), providerManager);
    }

    @Test
    void preHandle() throws IOException {
        HttpServletRequest request = MockAuthenticationRequest.createTokenRequest(EMAIL, PASSWORD);
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, null);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Map<String, Object> body = objectMapper.readValue(response.getContentAsString(), new TypeReference<HashMap<String,Object>>() {});
        assertThat(body.get("accessToken")).isEqualTo(JWT_TOKEN);
    }
}
