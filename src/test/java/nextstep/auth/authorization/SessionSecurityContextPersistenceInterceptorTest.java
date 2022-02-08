package nextstep.auth.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static nextstep.auth.authorization.step.SecurityContextStep.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("session SecurityContext 테스트")
class SessionSecurityContextPersistenceInterceptorTest {

    @DisplayName("session을 이용한 인증, SecurityContextHolder 정보를 세팅한다")
    @Test
    void preHandle() throws Exception {
        // given
        MockHttpServletRequest 요청 = session_인증_요청_mock();
        MockHttpServletResponse 응답 = 인증_응답_mock();
        SessionSecurityContextPersistenceInterceptor interceptor = new SessionSecurityContextPersistenceInterceptor();

        // when
        boolean preHandle = interceptor.preHandle(요청, 응답, new Object());

        // then
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(preHandle).isTrue();
    }

    @DisplayName("session에 인증 정보가 없는경우 ")
    @Test
    void preHandle_fail() throws Exception {
        // given
        MockHttpServletRequest 요청 = session_인증정보없음_요청_mock();
        MockHttpServletResponse 응답 = 인증_응답_mock();
        SessionSecurityContextPersistenceInterceptor interceptor = new SessionSecurityContextPersistenceInterceptor();

        // when
        boolean preHandle = interceptor.preHandle(요청, 응답, new Object());

        // then
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        assertThat(authentication).isNull();
        assertThat(preHandle).isTrue();
    }

}