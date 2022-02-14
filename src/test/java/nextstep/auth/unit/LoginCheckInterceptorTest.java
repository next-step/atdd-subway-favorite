package nextstep.auth.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.authorization.LoginCheckInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginCheckInterceptorTest extends AuthTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("로그인이 되어있는지 확인(Bearer token이 존재하고 유효함)")
    @Test
    void preHandle() throws Exception {
        // given
        LoginCheckInterceptor loginCheckInterceptor = new LoginCheckInterceptor(jwtTokenProvider);
        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        boolean preHandleBoolean = loginCheckInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(preHandleBoolean).isTrue();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, AGE);
        String payload = objectMapper.writeValueAsString(loginMember);
        String accessToken = jwtTokenProvider.createToken(payload);
        request.addHeader("Authorization", "Bearer " + accessToken);

        return request;
    }
}
