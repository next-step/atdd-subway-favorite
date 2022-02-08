package nextstep.subway.auth.authorization;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.auth.ui.authorization.SecurityContextInterceptor;
import nextstep.subway.auth.ui.authorization.session.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;

public class SessionSecurityContextPersistenceInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    SecurityContextInterceptor interceptor;
    MockHttpServletResponse response;
    LoginMember loginMember;

    @BeforeEach
    void setUp() {
        interceptor = new SessionSecurityContextPersistenceInterceptor();
        response = new MockHttpServletResponse();
        loginMember = new LoginMember(1L, EMAIL, PASSWORD, AGE);
    }

    @Test
    @DisplayName("세션에서 LoginMember 정보를 가져와서 SecurityContextHolder에 저장한다.")
    void preHandle() throws IOException {
        // given
        MockHttpServletRequest request = createRequest(loginMember);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(loginMember);
    }

    @Test
    @DisplayName("afterCompletion 호출 시 SecurityContextHolder가 초기화 된다.")
    void afterCompletion() throws Exception{
        // given
        MockHttpServletRequest request = createRequest(loginMember);
        interceptor.preHandle(request, response, new Object());

        // when
        interceptor.afterCompletion(request, response, new Object(), null);

        // then
        assertThat(SecurityContextHolder.getContext()).isNotNull();
    }

    private MockHttpServletRequest createRequest(LoginMember loginMember) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(new Authentication(loginMember)));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);
        return request;
    }
}
