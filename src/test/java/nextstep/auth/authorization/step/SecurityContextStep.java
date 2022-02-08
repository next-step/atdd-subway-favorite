package nextstep.auth.authorization.step;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.domain.LoginMember;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;

public class SecurityContextStep {

    public static final String AUTHORIZATION = "Authorization";
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SECURITY_CONTEXT";
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    private static final LoginMember LOGIN_MEMBER = new LoginMember(1L, EMAIL, PASSWORD, AGE);

    public static MockHttpServletRequest token_인증_요청_mock() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION, JWT_TOKEN);

        return request;
    }

    public static MockHttpServletRequest token_인증정보없음_요청_mock() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        return request;
    }

    public static MockHttpServletRequest session_인증_요청_mock() {
        SecurityContext securityContext = securityContext();

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(mockHttpSession);

        return request;
    }

    public static MockHttpServletRequest session_인증정보없음_요청_mock() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        return request;
    }

    public static MockHttpServletResponse 인증_응답_mock() {
        return new MockHttpServletResponse();
    }

    public static String 로그인멤버_json() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(LOGIN_MEMBER);
    }

    private static SecurityContext securityContext() {
        Authentication authentication = new Authentication(LOGIN_MEMBER);
        return new SecurityContext(authentication);
    }

}
