package nextstep.auth.unit.authentication.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.authentication.token.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
        CustomUserDetailsService customUserDetailsService = mock(CustomUserDetailsService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);

        LoginMember expectedMember = new LoginMember(-1L, EMAIL, PASSWORD, 0);
        Mockito.when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(expectedMember);
        Mockito.when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider, objectMapper, new TokenAuthenticationConverter(objectMapper));
    }

    @Test
    void authenticate() {
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        Authentication authentication = interceptor.authenticate(authenticationToken);

        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void preHandle() throws IOException {
        HttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, null);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Map<String, Object> body = objectMapper.readValue(response.getContentAsString(), new TypeReference<HashMap<String,Object>>() {});
        assertThat(body.get("accessToken")).isEqualTo(JWT_TOKEN);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
