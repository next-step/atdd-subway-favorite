package nextstep.auth.unit.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import org.apache.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class MockAuthorizationRequest {
    public static MockHttpServletRequest createSessionRequest(String username) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        Authentication authentication = new Authentication(username);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        request.setSession(session);
        return request;
    }

    public static MockHttpServletRequest createTokenRequest(String token) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return request;
    }
}
