package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static nextstep.subway.auth.utils.AuthorizationTestUtils.setBearerAuthorizationHeader;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("토큰 SecurityContext에 persist하는 인터셉터 테스트")
@ExtendWith(MockitoExtension.class)
class TokenSecurityContextPersistenceInterceptorTest {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private JwtTokenProvider tokenProvider;
    private TokenSecurityContextPersistenceInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        interceptor = new TokenSecurityContextPersistenceInterceptor(userDetailsService, tokenProvider);
        request = setBearerAuthorizationHeader(new MockHttpServletRequest(), ACCESS_TOKEN);
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("들어온 토큰이 유효하면 토큰 안에 payload를 SecurityContextHolder에 추가한다")
    void persistLoginMember() {
        //given
        LoginMember mockMember = new LoginMember(1L, EMAIL, PASSWORD, AGE);

        given(tokenProvider.validateToken(anyString())).willReturn(true);
        given(tokenProvider.getPayload(anyString())).willReturn(EMAIL);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mockMember);

        //when
        boolean preHandle = interceptor.preHandle(request, response, new Object());

        //then
        assertThat(preHandle).isTrue();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(mockMember);
    }

    @Test
    @DisplayName("들어온 토큰이 유효하지 않으면 SecurityContext에 추가하지 않는다")
    void notValidToken() {
        //given
        given(tokenProvider.validateToken(anyString())).willReturn(false);

        //when
        boolean preHandle = interceptor.preHandle(request, response, new Object());

        //then
        assertThat(preHandle).isTrue();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }

    @Test
    @DisplayName("afterCompletion 호출 시 SecurityContextHolder가 초기화 된다")
    void initSecurityContextHolder() {
        //given
        LoginMember mockMember = mock(LoginMember.class);
        SecurityContextHolder.setContext(new SecurityContext(new Authentication(mockMember)));

        //when
        interceptor.afterCompletion(request, response, new Object(), null);

        //then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}