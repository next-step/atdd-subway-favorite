package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.handler.BearerTokenAuthVerificationHandler;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.subway.fixture.MockMember.GUEST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BearerTokenAuthVerificationHandlerTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";


    @Test
    @DisplayName("Bearer 토큰을 전달하면 인증정보가 생성한다.")
    void createAuthenticationTest() {
        BearerTokenAuthVerificationHandler handler = new BearerTokenAuthVerificationHandler(jwtTokenProvider);
        when(jwtTokenProvider.validateToken(JWT_TOKEN)).thenReturn(true);
        when(jwtTokenProvider.getPrincipal(JWT_TOKEN)).thenReturn(GUEST.getEmail());
        when(jwtTokenProvider.getRoles(JWT_TOKEN)).thenReturn(GUEST.getAuthorities());

        Authentication authentication = handler.createAuthentication(createMockRequest("Bearer"));

        assertThat(authentication.getPrincipal()).isEqualTo(GUEST.getEmail());
        assertThat(authentication.getAuthorities()).isEqualTo(GUEST.getAuthorities());
    }


    @ParameterizedTest(name = "[{argumentsWithNames}] 다른 타입의 토큰을 전달하면 인증정보 생성에 실패한다.")
    @ValueSource(strings = {"Basic", "Digest", "HOBA", "Mutual"})
    void createAuthenticationFailTest(String tokenType) {
        BearerTokenAuthVerificationHandler handler = new BearerTokenAuthVerificationHandler(jwtTokenProvider);

        assertThatThrownBy(() -> handler.createAuthentication(createMockRequest(tokenType)))
                .isInstanceOf(AuthenticationException.class);
    }

    private MockHttpServletRequest createMockRequest(String authType) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, authType + " " + JWT_TOKEN);
        return request;
    }
}
