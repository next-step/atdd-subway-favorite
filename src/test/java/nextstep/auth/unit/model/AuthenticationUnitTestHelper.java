package nextstep.auth.unit.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class AuthenticationUnitTestHelper {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    private static final int AGE = 20;

    public static MockHttpServletRequest createMockTokenRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    public static MockHttpServletRequest createMockSessionRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, String[]> paramMap = new HashMap<>();
        paramMap.put(USERNAME_FIELD, new String[]{EMAIL});
        paramMap.put(PASSWORD_FIELD, new String[]{PASSWORD});
        request.setParameters(paramMap);
        return request;
    }

    public static AuthenticationToken getAuthenticationToken() {
        return new AuthenticationToken(EMAIL, PASSWORD);
    }

    public static LoginMember getUserDetails() {
        return new LoginMember(1L, EMAIL, PASSWORD, 20);
    }

    public static Member getMember() {
        Member member = new Member(EMAIL, PASSWORD, AGE);
        ReflectionTestUtils.setField(member, "id", 1L);
        return member;
    }

    public static MemberRequest getMemberRequest() {
        return new MemberRequest(EMAIL, PASSWORD, AGE);
    }

    public static String getPayload(LoginMember userDetails) throws IOException {
        return new ObjectMapper().writeValueAsString(userDetails);
    }
}
