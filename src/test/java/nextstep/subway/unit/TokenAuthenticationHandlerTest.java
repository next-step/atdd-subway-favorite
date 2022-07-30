package nextstep.subway.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.handler.TokenAuthenticationHandler;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
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
import java.util.Map;

import static nextstep.subway.fixture.MockMember.GUEST;
import static nextstep.subway.utils.UserUtils.createUser;
import static nextstep.subway.utils.UserUtils.createUserWithPassword;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationHandlerTest {
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("사용자 EMAIL과 PASSWORD가 일치하면 사용자를 반환한다.")
    void preAuthenticationTest() throws IOException {
        // given
        User mockUser = createUser(GUEST);

        when(userDetailsService.loadUserByUsername(GUEST.getEmail())).thenReturn(mockUser);
        TokenAuthenticationHandler handler = new TokenAuthenticationHandler(userDetailsService, jwtTokenProvider);

        // when
        User user = handler.preAuthentication(createMockRequest(GUEST));

        // then
        assertThat(user.getPrincipal()).isEqualTo(GUEST.getEmail());
        assertThat(user.getAuthorities()).isEqualTo(GUEST.getAuthorities());
    }

    @Test
    @DisplayName("사용자 EMAIL이 없으면 인증이 실패한다.")
    void preAuthenticationEmailFailTest() {
        // when
        when(userDetailsService.loadUserByUsername(GUEST.getEmail())).thenReturn(null);

        TokenAuthenticationHandler handler = new TokenAuthenticationHandler(userDetailsService, jwtTokenProvider);

        // then
        assertThatThrownBy(() -> handler.preAuthentication(createMockRequest(GUEST)))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("사용자 PASSWORD가 다르면 인증이 실패한다.")
    void preAuthenticationPasswordFailTest() {
        // when
        User user = createUserWithPassword(GUEST, "other_password");
        when(userDetailsService.loadUserByUsername(GUEST.getEmail())).thenReturn(user);

        TokenAuthenticationHandler handler = new TokenAuthenticationHandler(userDetailsService, jwtTokenProvider);

        // then
        assertThatThrownBy(() -> handler.preAuthentication(createMockRequest(GUEST)))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("token이 response에 담겨서 내려온다.")
    void afterAuthenticationTest() throws IOException {
        // given
        User user = createUser(GUEST);
        when(jwtTokenProvider.createToken(user.getPrincipal(), user.getAuthorities())).thenReturn(JWT_TOKEN);

        TokenAuthenticationHandler handler = new TokenAuthenticationHandler(userDetailsService, jwtTokenProvider);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        handler.afterAuthentication(user, response);
        String accessToken = getAccessToken(response);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(accessToken).isEqualTo(JWT_TOKEN);
    }

    private MockHttpServletRequest createMockRequest(MockMember member) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(member.getEmail(), member.getPassword());
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    @SuppressWarnings("unchecked")
    private String getAccessToken(MockHttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, String> body = new ObjectMapper().readValue(response.getContentAsString(), Map.class);
        return body.get("accessToken");
    }
}
