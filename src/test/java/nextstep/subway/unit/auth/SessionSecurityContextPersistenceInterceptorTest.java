package nextstep.subway.unit.auth;

import nextstep.auth.authorization.strategy.SecurityContextHolderStrategy;
import nextstep.auth.authorization.SecurityContextInterceptor;
import nextstep.auth.authorization.strategy.SessionSecurityContextHolderStrategy;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.context.SecurityContextHolder.*;
import static nextstep.subway.unit.auth.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class SessionSecurityContextPersistenceInterceptorTest {
    SecurityContextHolderStrategy securityContextHolderStrategy;
    SecurityContextInterceptor interceptor;
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    void setUp() throws IOException {
        securityContextHolderStrategy = new SessionSecurityContextHolderStrategy();
        interceptor = new SessionSecurityContextPersistenceInterceptor(securityContextHolderStrategy);
        request = createSessionMockRequest();
        response = createMockResponse();
    }

    @Test
    void afterCompletion() throws Exception {
        // given
        SecurityContext securityContext = new SecurityContext(createAuthentication());
        setContext(securityContext);

        // when
        interceptor.afterCompletion(request, response, new Object(), new RuntimeException());

        // then
        Authentication authentication = getContext().getAuthentication();
        assertThat(authentication).isNull();
    }

    @Test
    void preHandle() throws Exception {
        // given
        SecurityContext securityContext = new SecurityContext(createAuthentication());
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        Authentication authentication = getContext().getAuthentication();
        assertThat(authentication).isNotNull();
    }
}