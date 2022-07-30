package nextstep.subway.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @InjectMocks
    private TokenAuthenticationInterceptor interceptor;

    @Mock
    private LoginMemberService loginMemberService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() throws IOException {
        // given
        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void convert() throws IOException {
        // when
        TokenRequest tokenRequest = interceptor.convert(request);

        // then
        assertAll(
                () -> assertThat(tokenRequest.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(tokenRequest.getPassword()).isEqualTo(PASSWORD)
        );
    }

    @Test
    void authenticate() {
        // given
        given(loginMemberService.loadUserByUsername(anyString()))
                .willReturn(new LoginMember(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name())));

        // when
        LoginMember member = interceptor.authenticate(new TokenRequest(EMAIL, PASSWORD));

        // then
        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(member.checkPassword(PASSWORD)).isTrue(),
                () -> assertThat(member.getAuthorities()).isEqualTo(List.of(ROLE_MEMBER.name()))
        );
    }

    @Test
    void preHandle() throws Exception {
        // given
        given(loginMemberService.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name())));

        given(jwtTokenProvider.createToken(anyString(), any()))
                .willReturn(JWT_TOKEN);

        // when
        boolean result = interceptor.preHandle(request, response, null);

        // then
        assertThat(result).isFalse();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
