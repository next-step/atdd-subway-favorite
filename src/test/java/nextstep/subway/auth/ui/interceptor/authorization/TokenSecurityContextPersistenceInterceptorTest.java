package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.application.SecurityContextPersistenceHandler;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@DisplayName("토큰 유효성 검사를 수행하고 토큰의 정보를 이용해 사용자를 인증한다 ")
class TokenSecurityContextPersistenceInterceptorTest {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZW1haWwiOiJkaGxlZUB0ZXN0LmNvbSIsImFnZSI6MTAsImlhdCI6MTUxNjIzOTAyMn0.7mzHE7xlGhUU_FXS9gn7AhrNyFsmpb9zZhNCX3D4F9k";
    private static final String PAYLOAD = "{\"id\":1,\"email\":\"dhlee@email.com\",\"age\":10}";
    private static final String EMAIL = "dhlee@email.com";
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private TokenSecurityContextPersistenceInterceptor tokenSecurityContextPersistenceInterceptor;
    private SecurityContextPersistenceHandler persistenceHandler;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN);

        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(PAYLOAD);
        persistenceHandler = mock(SecurityContextPersistenceHandler.class);
        tokenSecurityContextPersistenceInterceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider, persistenceHandler);
    }

    @Test
    @DisplayName("토큰이 포함된 요청의 경우 token 정보를 바탕으로 SecurityContext에 인증정보를 저장한다")
    public void preHandleTest() throws Exception {
        doAnswer(invocation -> {
            SecurityContext securityContext = invocation.getArgument(0);
            Authentication authentication = securityContext.getAuthentication();
            assertThat(authentication).isNotNull();

            // principal 저장 여부 확
            Object principal = authentication.getPrincipal();
            assertThat(principal).isNotNull();
            assertThat(principal).isInstanceOf(LoginMember.class);

            // 계정 정보가 매칭되는지 확인
            LoginMember loginMember = (LoginMember) principal;
            assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
            return null;
        }).when(persistenceHandler).persist(any(SecurityContext.class));

        // when
        tokenSecurityContextPersistenceInterceptor.preHandle(request, response, new Object());

        // then
        // 인증 정보 저장 확인
        verify(persistenceHandler).persist(any(SecurityContext.class));
    }

    @Test
    void afterCompletionTest() {
        // given
        SecurityContextHolder.setContext(new SecurityContext(new Authentication()));

        // when
        tokenSecurityContextPersistenceInterceptor.afterCompletion(request, response, new Object(), null);

        // then
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        assertThat(authentication).isNull();
    }
}