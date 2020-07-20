package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.domain.LoginMember;
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

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password1234";

    private TokenSecurityContextPersistenceInterceptor interceptor;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
        this.response = new MockHttpServletResponse();
        this.objectMapper = new ObjectMapper();

        this.request = new MockHttpServletRequest();
        this.request.addHeader("Authorization", "Bearer qwerasdfzxcv");

        SecurityContextHolder.clearContext();
    }

    @DisplayName("JWT 토큰이 유효하지 않으면 SecurityContextHolder에 저장하지 않는다")
    @Test
    void invalidToken() throws Exception {
        // given
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        SecurityContext context = SecurityContextHolder.getContext();
        assertThat(context.getAuthentication()).isNull();
    }

    @DisplayName("JWT 토큰에서 payload를 추출하고 SecurityContextHolder에 저장한다")
    @Test
    void noSecurityContext() throws Exception {
        // given
        LoginMember member = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(objectMapper.writeValueAsString(member));

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        SecurityContext context = SecurityContextHolder.getContext();
        LoginMember contextMember = (LoginMember) context.getAuthentication().getPrincipal();
        assertThat(contextMember.getId()).isEqualTo(member.getId());
    }
}