package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @DisplayName("정상적인 요청값이 들어왔을 경우 인증 토큰을 받을수 있다")
    @Test
    void convert() throws IOException {
        // when
        final AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createMockRequest());

        // then
        assertAll(
                () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
        );
    }

    @DisplayName("정상적인 토큰이 들어왔을 경우 인증 객체를 받을 수 있다")
    @Test
    void authenticate() throws IOException {
        // given
        final AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createMockRequest());
        given(customUserDetailsService.loadUserByUsername(EMAIL)).willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        // when
        final Authentication authenticate = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(authenticate.getPrincipal()).isNotNull();
    }

    @DisplayName("정상적인 요청값이 들어왔을 경우 다음 작업 여부 진행하지 않는다를 반환한다")
    @Test
    void preHandle() throws IOException {
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
