package nextstep.subway.unit;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private TokenAuthenticationInterceptor interceptor;
    @Mock
    private LoginMemberService loginMemberService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() throws IOException {
        interceptor = new TokenAuthenticationInterceptor(loginMemberService, jwtTokenProvider, objectMapper);
        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void convert() throws IOException {
        TokenRequest tokenRequest = interceptor.convert(request);

        assertAll(() -> {
            assertThat(tokenRequest.getEmail()).isEqualTo(EMAIL);
            assertThat(tokenRequest.getPassword()).isEqualTo(PASSWORD);
        });
    }

    @Test
    void authenticate() {
        // given
        given(loginMemberService.loadUserByUsername(EMAIL))
            .willReturn(new LoginMember(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name())));
        LoginMember loginMember = interceptor.authenticate(new TokenRequest(EMAIL, PASSWORD));

        // then
        assertAll(() -> {
            assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
            assertThat(loginMember.checkPassword(PASSWORD)).isTrue();
        });
    }

    @Test
    void preHandle() throws Exception {
        given(loginMemberService.loadUserByUsername(EMAIL))
            .willReturn(new LoginMember(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name())));
        boolean result = interceptor.preHandle(request, response, null);

        assertThat(result).isFalse();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
