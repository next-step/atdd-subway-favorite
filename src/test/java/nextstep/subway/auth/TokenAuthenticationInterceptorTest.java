package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.token.TokenAuthenticationConverter;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 30;
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private CustomUserDetailsService customUserDetailsService;

    private JwtTokenProvider jwtTokenProvider;

    private AuthenticationConverter authenticationConverter;

    private TokenAuthenticationInterceptor interceptor;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.authenticationConverter = mock(AuthenticationConverter.class);
        this.customUserDetailsService = mock(CustomUserDetailsService.class);
        this.jwtTokenProvider = mock(JwtTokenProvider.class);
        this.interceptor = new TokenAuthenticationInterceptor(this.customUserDetailsService, new TokenAuthenticationConverter(objectMapper), jwtTokenProvider, objectMapper);
    }

    @Test
    void preHandle() throws IOException {
        //given
        MockHttpServletRequest httpServletRequest = createMockRequest();
        MockHttpServletResponse httpServletResponse = createMockResponse();
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(createMockLoginMember());
        when(authenticationConverter.convert(httpServletRequest)).thenReturn(new AuthenticationToken(EMAIL, JWT_TOKEN));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        //when
        interceptor.preHandle(httpServletRequest, httpServletResponse, new Object());

        //then
        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(httpServletResponse.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }

    private MockHttpServletResponse createMockResponse() {
        return new MockHttpServletResponse();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private LoginMember createMockLoginMember() {
        return new LoginMember(1L, EMAIL, PASSWORD, AGE);
    }
}
