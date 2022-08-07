package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.User;
import nextstep.auth.UserDetailsService;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.nonchain.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @Test
    void convert() throws IOException {
        //given
        MockHttpServletRequest mockRequest = createMockRequest();

        //when
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(mockRequest);

        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() {
        //given
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new User(EMAIL, PASSWORD, List.of(RoleType.ROLE_ADMIN.name())));

        //when
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(new AuthenticationToken(EMAIL, PASSWORD));

        //then
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getAuthorities()).contains(RoleType.ROLE_ADMIN.name());
    }

    @Test
    void preHandle() throws Exception {
        //given
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new User(EMAIL, PASSWORD, List.of(RoleType.ROLE_ADMIN.name())));
        when(jwtTokenProvider.createToken(any(), any())).thenReturn(JWT_TOKEN);
        MockHttpServletResponse mockResponse = createMockResponse();
        MockHttpServletRequest mockRequest = createMockRequest();

        //when
        boolean isChain = tokenAuthenticationInterceptor.preHandle(mockRequest, mockResponse, new Object());

        //then
        assertThat(isChain).isFalse();
        assertThat(mockResponse.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private MockHttpServletResponse createMockResponse() throws IOException {
        return new MockHttpServletResponse();
    }

}
