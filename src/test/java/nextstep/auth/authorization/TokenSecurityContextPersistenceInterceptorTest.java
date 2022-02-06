package nextstep.auth.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.domain.Authentication;
import nextstep.auth.infrastructure.JwtTokenProvider;
import nextstep.auth.infrastructure.SecurityContextHolder;
import nextstep.auth.ui.authorization.SecurityContextInterceptor;
import nextstep.auth.ui.authorization.token.TokenSecurityContextPersistenceInterceptor;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenSecurityContextPersistenceInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    SecurityContextInterceptor interceptor;
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    LoginMember loginMember;

    @Spy
    ObjectMapper objectMapper;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider, objectMapper);
        response = new MockHttpServletResponse();
        loginMember = new LoginMember(1L, EMAIL, PASSWORD, AGE);

        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + JWT_TOKEN);
    }

    @Test
    @DisplayName("유효한 토큰이면 토큰 안의 payload를 SecurityContextHolder에 저장한다.")
    void preHandle() throws IOException {
        // given
        String payload = objectMapper.writeValueAsString(loginMember);
        when(jwtTokenProvider.validateToken(JWT_TOKEN)).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(payload);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
    }

    @Test
    @DisplayName("들어온 토큰이 유효하지 않으면 SecurityContextHolder에 저장하지 않는다.")
    void preHandleWithInvalidToken() throws IOException{
        // given
        when(jwtTokenProvider.validateToken(JWT_TOKEN)).thenReturn(false);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("afterCompletion 호출 시 SecurityContextHolder가 초기화 된다.")
    void afterCompletion() throws Exception{
        // given
        interceptor.preHandle(request, response, new Object());

        // when
        interceptor.afterCompletion(request, response, new Object(), null);

        // then
        assertThat(SecurityContextHolder.getContext()).isNotNull();
    }
}
