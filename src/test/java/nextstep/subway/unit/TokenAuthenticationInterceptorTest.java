package nextstep.subway.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.interceptor.AuthenticationInterceptor;
import nextstep.auth.authentication.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.domain.member.domain.LoginMember;
import nextstep.domain.member.service.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationInterceptor authenticationInterceptor;
    private ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void setUp() {
        authenticationInterceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, objectMapper);
    }

    @DisplayName("로그인 정보 추출 테스트")
    @Test
    void convert() throws IOException {
        //given
        MockHttpServletRequest request = createMockRequest();

        //when
        AuthenticationToken token = authenticationInterceptor.convert(request);

        //then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("인증 테스트")
    @Test
    void authenticate() throws JsonProcessingException {
        //given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        //when
        Authentication authentication = authenticationInterceptor.authenticate(authenticationToken);

        //then
        LoginMember principal = (LoginMember) authentication.getPrincipal();
        assertThat(principal).isNotNull();
        assertThat(principal.checkPassword(PASSWORD)).isTrue();
    }

    @Test
    void preHandle() throws IOException {
        //given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);
        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        authenticationInterceptor.preHandle(request, response, new Object());

        //then
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
