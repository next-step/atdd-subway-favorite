package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.convertor.TokenConvertor;
import nextstep.auth.authentication.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private TokenAuthenticationInterceptor interceptor;
    private ObjectMapper objectMapper = new ObjectMapper();

    private TokenConvertor tokenConvertor = new TokenConvertor(objectMapper);

    public static MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    @BeforeEach
    void setUp() {
        interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider, objectMapper);
    }

    @Test
    void convert() throws IOException {
        //given
        MockHttpServletRequest mockRequest = createMockRequest();
        //when
        AuthenticationToken authenticationToken = tokenConvertor.convert(mockRequest);
        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() throws IOException {
        //given
        MockHttpServletRequest mockRequest = createMockRequest();
        AuthenticationToken authenticationToken = tokenConvertor.convert(mockRequest);
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(LoginMember.of(new Member(EMAIL, PASSWORD, 3)));
        //when
        Authentication authenticate = interceptor.authenticate(authenticationToken);
        //then
        assertNotNull(authenticate);
    }

    @Test
    void preHandle() throws IOException {
        //given
        MockHttpServletRequest mockRequest = createMockRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(LoginMember.of(new Member(EMAIL, PASSWORD, 3)));

        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);
        //when
        boolean actual = interceptor.preHandle(mockRequest, mockResponse, new Object());
        //then
        assertThat(actual).isEqualTo(false);
    }

}
