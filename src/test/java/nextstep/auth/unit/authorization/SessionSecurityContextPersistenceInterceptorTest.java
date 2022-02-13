package nextstep.auth.unit.authorization;

import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionSecurityContextPersistenceInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private SessionSecurityContextPersistenceInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new SessionSecurityContextPersistenceInterceptor();
    }

    @Test
    void preHandle() {
        MockHttpServletRequest request = MockAuthorizationRequest.createSessionRequest(EMAIL);
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
    }

    @Test
    void afterCompletion() {
        MockHttpServletRequest request = MockAuthorizationRequest.createSessionRequest(EMAIL);
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);

        interceptor.afterCompletion(request, response, null, null);
        Authentication updatedAuthentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(updatedAuthentication).isNull();
    }
}
