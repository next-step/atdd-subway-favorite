package nextstep.subway.unit;

import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.unit.MockHttpRequestFixtures.createSessionMockRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class SessionAuthenticationInterceptorTest extends AuthenticationInterceptorTestSupport {
    @InjectMocks
    private SessionAuthenticationInterceptor interceptor;

    @Test
    void preHandle() {
        // given
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(FakePasswordCheckableUser.create(EMAIL, PASSWORD));
        HttpServletRequest request = createSessionMockRequest(EMAIL, PASSWORD);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when then
        assertDoesNotThrow(() -> interceptor.preHandle(request, response, null));
    }

}
