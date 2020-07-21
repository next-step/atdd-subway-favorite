package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final String CREDENTIALS = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTk1MzM3MzEzfQ.wDN4IDXZluCbjthr2i9qrYUcsJXCcMuK_0ivAL4U44I";
    private static final String PAYLOAD = "{\"sub\":\"subway.com\",\"name\":\"DaHyeon Lee\",\"iat\":1595337313}";
    private static final String EMAIL = "dhlee@email.com";
    private static final String PASSWORD = "test1234";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private TokenSecurityContextPersistenceInterceptor tokenSecurityContextPersistenceInterceptor;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + CREDENTIALS);

        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(PAYLOAD);
        tokenSecurityContextPersistenceInterceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
    }

    @Test
    @DisplayName("토큰이 포함된 요청의 경우 token 정보를 바탕으로 SecurityContext에 인증정보를 저장한다")
    public void preHandleTest() throws JsonProcessingException {
        // when
        tokenSecurityContextPersistenceInterceptor.preHandle(request, response, new Object());

        // then
        // 인증 정보 저장 확인
        // 여기에 하나 궁금해지는게 외부의 동작을 알고 검증하는게 과연 좋은 테스트 일가
        // 차라리 setContext를 powermock으로 검증하는게 맞을까....
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();

        // principal 저장 여부 확
        Object principal = authentication.getPrincipal();
        assertThat(principal).isNotNull();
        assertThat(principal).isInstanceOf(LoginMember.class);

        // 계정 정보가 매칭되는지 확인
        LoginMember loginMember = (LoginMember) principal;
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getPassword()).isEqualTo(PASSWORD);
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