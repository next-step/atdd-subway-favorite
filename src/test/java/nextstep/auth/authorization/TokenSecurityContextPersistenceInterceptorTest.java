package nextstep.auth.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static nextstep.auth.authorization.step.SecurityContextStep.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@DisplayName("token SecurityContext 테스트")
@ExtendWith(MockitoExtension.class)
class TokenSecurityContextPersistenceInterceptorTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("token 이용한 인증, SecurityContextHolder 정보를 세팅한다")
    @Test
    void preHandle() throws Exception {
        // given
        MockHttpServletRequest 요청 = token_인증_요청_mock();
        MockHttpServletResponse 응답 = 인증_응답_mock();
        TokenSecurityContextPersistenceInterceptor interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
        Mockito.when(jwtTokenProvider.validateToken(anyString()))
                .thenReturn(true);
        Mockito.when(jwtTokenProvider.getPayload(anyString()))
                .thenReturn(로그인멤버_json());

        // when
        boolean preHandle = interceptor.preHandle(요청, 응답, new Object());

        // then
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(preHandle).isTrue();
    }

    @DisplayName("token에 인증 정보가 없는 경우")
    @Test
    void preHandle_fail() throws Exception {
        // given
        MockHttpServletRequest 요청 = token_인증정보없음_요청_mock();
        MockHttpServletResponse 응답 = 인증_응답_mock();
        TokenSecurityContextPersistenceInterceptor interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);

        // when
        boolean preHandle = interceptor.preHandle(요청, 응답, new Object());

        // then
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        assertThat(authentication).isNull();
        assertThat(preHandle).isTrue();
    }

}