package nextstep.auth.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authorization.LoginCheckInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginCheckInterceptorTest extends AuthTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginCheckInterceptor loginCheckInterceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        super.setUp();
        loginCheckInterceptor = new LoginCheckInterceptor(jwtTokenProvider);
        response = new MockHttpServletResponse();
    }

    @DisplayName("로그인이 되어있는지 확인(Bearer token이 존재하고 유효함)")
    @Test
    void preHandle() throws Exception {
        // given
        request = createMockRequest();

        // when
        boolean result = loginCheckInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("로그인이 되어있는지 확인(Bearer token이 존재하고 유효하지않음)")
    @Test
    void preHandle2() throws Exception {
        // given
        request = createMockRequestWithInvalidAccessToken();

        // when
        boolean result = loginCheckInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("로그인이 되어있는지 확인(Authorization이 존재하지 않음)")
    @Test
    void preHandle3() throws Exception {
        // given
        request = new MockHttpServletRequest();

        // when
        boolean result = loginCheckInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(result).isFalse();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, AGE);
        String payload = objectMapper.writeValueAsString(loginMember);
        String accessToken = jwtTokenProvider.createToken(payload);
        request.addHeader("Authorization", "Bearer " + accessToken);

        return request;
    }

    private MockHttpServletRequest createMockRequestWithInvalidAccessToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalidAccessToken");

        return request;
    }
}
