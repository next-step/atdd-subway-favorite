package nextstep.subway.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.handler.TokenAuthenticationHandler;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import nextstep.subway.fixture.MockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationHandlerTest {
    private static final String EMAIL = MockMember.GUEST.getEmail();
    private static final String PASSWORD = MockMember.GUEST.getPassword();
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("사용자 EMAIL과 PASSWORD가 일치하면 사용자를 반환한다.")
    void preAuthenticationTest() throws IOException {
        // given
        List<String> memberRoles = List.of(RoleType.ROLE_MEMBER.name());
        LoginMember loginMember = new LoginMember(EMAIL, PASSWORD, memberRoles);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        TokenAuthenticationHandler tokenAuthenticationHandler = new TokenAuthenticationHandler(userDetailsService, jwtTokenProvider);

        // when
        User user = tokenAuthenticationHandler.preAuthentication(createMockRequest());

        // then
        assertThat(user.getPrincipal()).isEqualTo(EMAIL);
        assertThat(user.getAuthorities()).isEqualTo(memberRoles);
    }

    @Test
    @DisplayName("사용자 EMAIL이 없으면 인증이 실패한다.")
    void preAuthenticationEmailFailTest() throws IOException {
        // when
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(null);

        TokenAuthenticationHandler tokenAuthenticationHandler = new TokenAuthenticationHandler(userDetailsService, jwtTokenProvider);

        // then
        assertThatThrownBy(() -> tokenAuthenticationHandler.preAuthentication(createMockRequest()))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("사용자 PASSWORD가 다르면 인증이 실패한다.")
    void preAuthenticationPasswordFailTest() throws IOException {
        // when
        LoginMember loginMember = new LoginMember(EMAIL, "other_password", List.of(RoleType.ROLE_MEMBER.name()));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);

        TokenAuthenticationHandler tokenAuthenticationHandler = new TokenAuthenticationHandler(userDetailsService, jwtTokenProvider);

        // then
        assertThatThrownBy(() -> tokenAuthenticationHandler.preAuthentication(createMockRequest()))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("token이 response에 담겨서 내려온다.")
    void afterAuthenticationTest() throws IOException {
        // given
        List<String> memberRoles = List.of(RoleType.ROLE_MEMBER.name());
        LoginMember loginMember = new LoginMember(EMAIL, PASSWORD, memberRoles);
        when(jwtTokenProvider.createToken(loginMember.getPrincipal(), memberRoles)).thenReturn(JWT_TOKEN);

        TokenAuthenticationHandler tokenAuthenticationHandler = new TokenAuthenticationHandler(userDetailsService, jwtTokenProvider);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        tokenAuthenticationHandler.afterAuthentication(loginMember, response);
        String accessToken = getAccessToken(response);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(accessToken).isEqualTo(JWT_TOKEN);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }


    @SuppressWarnings("unchecked")
    private String getAccessToken(MockHttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, String> body = new ObjectMapper().readValue(response.getContentAsString(), Map.class);
        return body.get("accessToken");
    }
}
