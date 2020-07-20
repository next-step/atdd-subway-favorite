package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenSecurityContextPersistenceInterceptorTest {

    private TokenSecurityContextPersistenceInterceptor interceptor;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        this.interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
        this.response = new MockHttpServletResponse();

        this.request = new MockHttpServletRequest();
        this.request.addHeader("Authorization", "Bearer qwerasdfzxcv");

        SecurityContextHolder.clearContext();
    }

    @DisplayName("JWT 토큰이 유효하지 않으면 SecurityContextHolder에 저장하지 않는다")
    @Test
    void throwException_invalidToken() throws Exception {
        // given
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        SecurityContext context = SecurityContextHolder.getContext();
        assertThat(context.getAuthentication()).isNull();
    }
}