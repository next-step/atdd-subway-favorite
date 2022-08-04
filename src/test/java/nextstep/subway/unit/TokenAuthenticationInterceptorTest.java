package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.User;
import nextstep.auth.UserDetailsService;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @InjectMocks
    private TokenAuthenticationInterceptor interceptor;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private HttpServletResponse response;

    @BeforeEach
    void init() {
        when(jwtTokenProvider.createToken(EMAIL, List.of(ROLE_MEMBER.name()))).thenReturn(JWT_TOKEN);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new User(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name())));

        response = new MockHttpServletResponse();
    }

    @DisplayName("등록되어있는 유저의 인증 정보가 담긴 요청을 처리하면 False를 반환한다.")
    @Test
    void preHandleRegisteredUser() throws Exception {
        boolean result = interceptor.preHandle(createMockRequest(EMAIL), response, null);
        assertThat(result).isFalse();
    }

    @DisplayName("등록되어있지 않은 유저의 인증 정보가 담긴 요청을 처리하면 True를 반환한다.")
    @Test
    void preHandleNonRegisteredUser() throws Exception {
        boolean result = interceptor.preHandle(createMockRequest("example"), response, null);
        assertThat(result).isTrue();
    }

    private MockHttpServletRequest createMockRequest(String email) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(email, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
