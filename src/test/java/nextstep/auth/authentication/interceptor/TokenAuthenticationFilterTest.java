package nextstep.auth.authentication.interceptor;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.user.User;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.LoginMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationFilterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @InjectMocks
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Mock
    private LoginMemberService loginMemberService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() throws IOException {
        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void convert() throws Exception {
        AuthenticationToken authenticationToken = tokenAuthenticationFilter.convert(request);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() {
        User user = new User(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name()));

        Authentication authenticate = tokenAuthenticationFilter.authenticate(user, new AuthenticationToken(EMAIL, PASSWORD));

        assertThat(authenticate.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticate.getAuthorities()).isEqualTo(List.of(ROLE_MEMBER.name()));
    }

    @Test
    void preHandle() throws Exception {
        User user = new User(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name()));
        given(loginMemberService.loadUserByUsername(EMAIL)).willReturn(user);
        given(jwtTokenProvider.createToken(anyString(), any())).willReturn(JWT_TOKEN);

        boolean result = tokenAuthenticationFilter.preHandle(request, response, null);

        assertThat(result).isFalse();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
